
package org.mds.net.http.headers;

import org.mds.net.util.CacheControl;
import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author
 */
final class CacheControlHeader extends AbstractHeader<CacheControl> {

    CacheControlHeader() {
        super(CacheControl.class, HttpHeaders.Names.CACHE_CONTROL);
    }

    @Override
    public String toString(CacheControl value) {
        return value.toString();
    }

    @Override
    public CacheControl toValue(String value) {
        return CacheControl.fromString(value);
    }

}
