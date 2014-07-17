
package org.mds.net.http.headers;

/**
 *
 * @author
 */
class IntHeader extends AbstractHeader<Integer> {

    IntHeader(String name) {
        super(Integer.TYPE, name);
    }

    @Override
    public String toString(Integer value) {
        return value.toString();
    }

    @Override
    public Integer toValue(String value) {
        return Integer.parseInt(value);
    }

}
