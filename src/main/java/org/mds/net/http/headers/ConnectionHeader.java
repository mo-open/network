
package org.mds.net.http.headers;

import org.mds.net.util.Connection;
import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author
 */
final class ConnectionHeader extends AbstractHeader<Connection> {

    ConnectionHeader() {
        super(Connection.class, HttpHeaders.Names.CONNECTION);
    }

    @Override
    public String toString(Connection value) {
        return value.toString();
    }

    @Override
    public Connection toValue(String value) {
        for (Connection c : Connection.values()) {
            if (value.toLowerCase().equals(c.toString())) {
                return c;
            }
        }
        return null;
    }

}
