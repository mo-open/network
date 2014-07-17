
package org.mds.net.http.headers;

import com.google.common.net.MediaType;
import io.netty.handler.codec.http.HttpHeaders;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
final class MediaTypeHeader extends AbstractHeader<MediaType> {

    MediaTypeHeader() {
        super(MediaType.class, HttpHeaders.Names.CONTENT_TYPE);
    }

    @Override
    public String toString(MediaType value) {
        return value.toString();
    }

    @Override
    public MediaType toValue(String value) {
        try {
            return MediaType.parse(value);
        } catch (IllegalArgumentException e) {
            Logger.getLogger(MediaTypeHeader.class.getName()).log(Level.WARNING, "Bad media type {0}", value);
            return null;
        }
    }

}
