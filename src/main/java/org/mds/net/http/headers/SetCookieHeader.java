
package org.mds.net.http.headers;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.ServerCookieEncoder;

/**
 *
 * @author
 */
final class SetCookieHeader extends AbstractHeader<Cookie> {

    SetCookieHeader() {
        super(Cookie.class, HttpHeaders.Names.SET_COOKIE.toString());
    }

    @Override
    public String toString(Cookie value) {
        return ServerCookieEncoder.encode(value);
    }

    @Override
    public Cookie toValue(String value) {
        return CookieDecoder.decode(value).iterator().next();
    }

}
