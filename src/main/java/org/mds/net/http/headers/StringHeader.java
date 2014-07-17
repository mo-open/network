
package org.mds.net.http.headers;

/**
 *
 * @author
 */
class StringHeader extends AbstractHeader<String> {

    StringHeader(String name) {
        super(String.class, name);
    }

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public String toValue(String value) {
        return value;
    }

}
