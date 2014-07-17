
package org.mds.net.http.headers;

import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author
 */
final class WebSocketProtocolsHeader extends AbstractHeader<String[]> {

    WebSocketProtocolsHeader() {
        super(String[].class, HttpHeaders.Names.WEBSOCKET_PROTOCOL.toString());
    }

    @Override
    public String toString(String[] value) {
        StringBuilder sb = new StringBuilder();
        for (String s : value) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public String[] toValue(String value) {
        String[] result = value.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }

}
