
package org.mds.net.http.headers;

import io.netty.handler.codec.http.ClientCookieEncoder;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author
 */
final class CookieHeader extends AbstractHeader<Cookie[]> {

    CookieHeader() {
        super(Cookie[].class, HttpHeaders.Names.COOKIE);
    }

    @Override
    public String toString(Cookie[] value) {
        return ClientCookieEncoder.encode(value);
    }

    @Override
    public Cookie[] toValue(String value) {
        return CookieDecoder.decode(value).toArray(new Cookie[0]);
    }

}
