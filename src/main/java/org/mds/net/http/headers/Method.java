
package org.mds.net.http.headers;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Enum of standard HTTP methods
 *
 * @author
 */
public enum Method implements org.mds.net.util.HttpMethod {

    GET, PUT, POST, OPTIONS, HEAD, DELETE, TRACE, UNKNOWN;

    public static Method get(HttpRequest req) {
        HttpMethod m = req.getMethod();
        return Method.valueOf(m.name().toUpperCase());
    }
}
