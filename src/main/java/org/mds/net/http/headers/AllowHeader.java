
package org.mds.net.http.headers;

import io.netty.handler.codec.http.HttpHeaders;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
final class AllowHeader extends AbstractHeader<Method[]> {

    AllowHeader(boolean isAllowOrigin) {
        super(Method[].class, isAllowOrigin ? "Access-Control-Allow-Methods" : HttpHeaders.Names.ALLOW);
    }

    @Override
    public String toString(Method[] value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < value.length; i++) {
            sb.append(value[i].name());
            if (i != value.length - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public Method[] toValue(String value) {
        String[] s = value.split(",");
        Method[] result = new Method[s.length];
        for (int i = 0; i < s.length; i++) {
            try {
                result[i] = Method.valueOf(s[i]);
            } catch (Exception e) {
                Logger.getLogger(AllowHeader.class.getName()).log(Level.INFO, "Bad methods in allow header '" + value + "'", e);
                return null;
            }
        }
        return result;
    }

}
