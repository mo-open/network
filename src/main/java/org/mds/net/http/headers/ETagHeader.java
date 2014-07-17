
package org.mds.net.http.headers;

/**
 *
 * @author
 */
class ETagHeader extends AbstractHeader<String> {

    ETagHeader(String name) {
        super(String.class, name);
    }

    @Override
    public String toString(String value) {
        return '"' + value + '"';
    }

    @Override
    public String toValue(String value) {
        if (value.length() > 1) {
            if (value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }

}
