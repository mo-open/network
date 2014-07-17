
package org.mds.net.http.headers;

/**
 *
 * @author
 */
class HeaderNamesHeader extends AbstractHeader<HeaderValueType<?>[]> {

    @SuppressWarnings(value = "unchecked")
    HeaderNamesHeader(String name) {
        super((Class) HeaderValueType[].class, name);
    }

    @Override
    public String toString(HeaderValueType[] value) {
        StringBuilder sb = new StringBuilder();
        for (HeaderValueType t : value) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(t.name());
        }
        return sb.toString();
    }

    @Override
    public HeaderValueType<?>[] toValue(String value) {
        if (value == null || value.isEmpty()) {
            return new HeaderValueType<?>[0];
        }
        String[] items = value.split(",");
        HeaderValueType<?>[] result = new HeaderValueType<?>[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = Headers.stringHeader(items[i]);
        }
        return result;
    }
}
