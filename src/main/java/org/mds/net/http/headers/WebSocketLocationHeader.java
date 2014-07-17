
package org.mds.net.http.headers;

import org.mds.net.util.Exceptions;
import io.netty.handler.codec.http.HttpHeaders;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author
 */
final class WebSocketLocationHeader extends AbstractHeader<URL> {

    public WebSocketLocationHeader() {
        super(URL.class, HttpHeaders.Names.WEBSOCKET_LOCATION.toString());
    }

    @Override
    public String toString(URL value) {
        return value.toExternalForm();
    }

    @Override
    public URL toValue(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException ex) {
            return Exceptions.chuck(ex);
        }
    }

}
