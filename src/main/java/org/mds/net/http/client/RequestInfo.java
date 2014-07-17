
package org.mds.net.http.client;

import org.mds.net.url.URL;
import io.netty.handler.codec.http.HttpRequest;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

final class RequestInfo {
    final URL url;
    final HttpRequest req;
    final AtomicBoolean cancelled;
    final ResponseFuture handle;
    final ResponseHandler<?> r;
    final AtomicInteger redirectCount = new AtomicInteger();
    volatile boolean listenerAdded;
    public RequestInfo(URL url, HttpRequest req, AtomicBoolean cancelled, ResponseFuture handle, ResponseHandler<?> r) {
        this.url = url;
        this.req = req;
        this.cancelled = cancelled;
        this.handle = handle;
        this.r = r;
    }
    
    @Override
    public String toString() {
        return "RequestInfo{" + "url=" + url + ", req=" + req + ", cancelled=" 
                + cancelled + ", handle=" + handle + ", r=" + r + '}';
    }
}
