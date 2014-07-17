
package org.mds.net.http.headers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
final class UriHeader extends AbstractHeader<URI> {

    UriHeader(String name) {
        super(URI.class, name);
    }

    @Override
    public String toString(URI value) {
        return value.toString();
    }

    @Override
    public URI toValue(String value) {
        try {
            return new URI(value);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Headers.class.getName()).log(Level.SEVERE, "Bad URI in " + name() + " - " + value, ex);
            return null;
        }
    }

}
