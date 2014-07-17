
package org.mds.net.http.headers;

import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author
 */
final class VaryHeader extends AbstractHeader<HeaderValueType[]> {

    VaryHeader() {
        super(HeaderValueType[].class, HttpHeaders.Names.VARY.toString());
    }

    @Override
    public String toString(HeaderValueType[] value) {
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
    public HeaderValueType<?>[] toValue(String value) {
        String[] s = value.split(",");
        HeaderValueType<?>[] result = new HeaderValueType<?>[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = new StringHeader(s[i].trim());
        }
        return result;
    }

}
