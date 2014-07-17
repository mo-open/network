package org.mds.net.http.client;

import org.mds.net.http.headers.Headers;
import org.mds.net.http.headers.Method;
import org.mds.net.url.URL;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author
 */
@Sharable
final class MessageHandlerImpl extends ChannelInboundHandlerAdapter {

    private final boolean followRedirects;
    private final HttpClient client;

    MessageHandlerImpl(boolean followRedirects, HttpClient client) {
        this.followRedirects = followRedirects;
        this.client = client;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        RequestInfo info = ctx.channel().attr(HttpClient.KEY).get();
        if (!info.cancelled.get()) {
            // Premature close, which is a legitimate way of ending a request
            // with no chunks and no Content-Length header according to RFC
            try {
                sendFullResponse(ctx);
            } finally {
                info.handle.event(new State.Closed());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        RequestInfo info = ctx.channel().attr(HttpClient.KEY).get();
        info.handle.event(new State.Error(cause));
        cause.printStackTrace();
    }

    private boolean checkCancelled(ChannelHandlerContext ctx) {
        RequestInfo info = ctx.channel().attr(HttpClient.KEY).get();
        boolean result = info.cancelled.get();
        if (result) {
            Channel ch = ctx.channel();
            if (ch.isOpen()) {
                ch.close();
            }
        }
        return result;
    }

    static class ResponseState {

        final CompositeByteBuf content = Unpooled.compositeBuffer();
//        final ByteBuf content = Unpooled.buffer();
        volatile HttpResponse resp;
        volatile boolean fullResponseSent;

        boolean hasResponse() {
            return resp != null;
        }
    }

    public static final AttributeKey<ResponseState> RS = AttributeKey.<ResponseState>valueOf("state");

    private ResponseState state(ChannelHandlerContext ctx) {
        Attribute<ResponseState> st = ctx.channel().attr(RS);
        ResponseState rs = st.get();
        if (rs == null) {
            rs = new ResponseState();
            st.set(rs);
        }
        return rs;
    }

    private String isRedirect(RequestInfo info, HttpResponse msg) throws UnsupportedEncodingException {
        HttpResponseStatus status = msg.getStatus();
        switch (status.code()) {
            case 300:
            case 301:
            case 302:
            case 303:
            case 305:
            case 307:
                String hdr = URLDecoder.decode(msg.headers().get(HttpHeaders.Names.LOCATION), "UTF-8");
                if (hdr != null) {
                    if (hdr.toLowerCase().startsWith("http://") || hdr.toLowerCase().startsWith("https://")) {
                        return hdr;
                    } else {
                        URL orig = info.url;
                        String pth = orig.getPath() == null ? "/" : URLDecoder.decode(orig.getPath().toString(), "UTF-8");
                        if (hdr.startsWith("/")) {
                            pth = hdr;
                        } else {
                            if (pth.endsWith("/")) {
                                pth += hdr;
                            } else {
                                pth += "/" + hdr;
                            }
                        }
                        StringBuilder sb = new StringBuilder(orig.getProtocol().toString());
                        sb.append("://").append(orig.getHost());
                        if (!orig.getProtocol().getDefaultPort().equals(orig.getPort())) {
                            sb.append(":").append(orig.getPort());
                        }
                        if (pth.charAt(0) != '/') {
                            sb.append('/');
                        }
                        sb.append(pth);
                        return sb.toString();
                    }
                }
            default:
                return null;
        }
    }

    public static final int MAX_REDIRECTS = 15;

    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        final RequestInfo info = ctx.channel().attr(HttpClient.KEY).get();
        if (checkCancelled(ctx)) {
            return;
        }
        final ResponseState state = state(ctx);
        if (msg instanceof HttpResponse) {
            state.resp = (HttpResponse) msg;
            if (followRedirects) {
                String redirUrl = isRedirect(info, state.resp);
                if (redirUrl != null) {
                    Method meth = state.resp.getStatus().code() == 303 ? Method.GET : Method.valueOf(info.req.getMethod().name());
                    // Shut off events from the old request
                    AtomicBoolean ab = new AtomicBoolean(true);
                    RequestInfo b = new RequestInfo(info.url, info.req, ab, new ResponseFuture(ab), null);
                    ctx.channel().attr(HttpClient.KEY).set(b);
                    info.handle.event(new State.Redirect(URL.parse(redirUrl)));
                    info.handle.cancelled = new AtomicBoolean();
                    if (info.redirectCount.getAndIncrement() < MAX_REDIRECTS) {
                        client.redirect(meth, URL.parse(redirUrl), info);
                        return;
                    } else {
                        info.handle.event(new State.Error(new IOException("Redirect loop to " + redirUrl)));
                        info.handle.cancelled.lazySet(true);
                    }
                }
            }
            info.handle.event(new State.HeadersReceived(state.resp));
        } else if (msg instanceof HttpContent) {
            HttpContent c = (HttpContent) msg;
            info.handle.event(new State.ContentReceived(c));
            c.content().resetReaderIndex();
            if (c.content().readableBytes() > 0) {
                state.content.writeBytes(c.content());
            }
            state.content.resetReaderIndex();
            boolean last = c instanceof LastHttpContent;
            if (!last && state.resp.headers().get(HttpHeaders.Names.CONTENT_LENGTH) != null) {
                long len = Headers.CONTENT_LENGTH.toValue(state.resp.headers().get(HttpHeaders.Names.CONTENT_LENGTH));
                last = state.content.readableBytes() >= len;
            }
            if (last) {
                c.content().resetReaderIndex();
                info.handle.event(new State.FullContentReceived(state.content));
                sendFullResponse(ctx);
            }
        }
//        if (!info.listenerAdded) {
//            info.listenerAdded = true;
//            // Handle the case of really old HTTP 1.0 style requests where there
//            // is no content-length, and the response is terminated by the server
//            // closing the connection
//            ctx.channel().closeFuture().addListener(new ChannelFutureListener(){
//
//                @Override
//                public void operationComplete(ChannelFuture f) throws Exception {
//                    state.content.resetReaderIndex();
//                    info.handle.event(new State.Closed());
//                    System.out.println("Termination close");
//                    sendFullResponse(ctx);
//                }
//            });
//        }
    }

    void sendFullResponse(ChannelHandlerContext ctx) {
        ResponseState state = state(ctx);
        RequestInfo info = ctx.channel().attr(HttpClient.KEY).get();
        Class<? extends State<?>> type = State.Finished.class;
        state.content.resetReaderIndex();
        if ((info.r != null || info.handle.has(type)) && !state.fullResponseSent && state.content.readableBytes() > 0) {
            state.fullResponseSent = true;
            info.handle.event(new State.FullContentReceived(state.content));
            DefaultFullHttpResponse full = new DefaultFullHttpResponse(state.resp.getProtocolVersion(), state.resp.getStatus(), state.content);
            for (Map.Entry<String, String> e : state.resp.headers().entries()) {
                full.headers().add(e.getKey(), e.getValue());
            }
            state.content.resetReaderIndex();

            if (info.r != null) {
                info.r.internalReceive(state.resp.getStatus(), state.resp.headers(), state.content);
            }
            state.content.resetReaderIndex();
            info.handle.event(new State.Finished(full));
            state.content.resetReaderIndex();
            info.handle.trigger();
        }
    }
}
