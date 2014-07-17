
package org.mds.net.http.headers;

/**
 *
 * @author
 */
class LongHeader extends AbstractHeader<Long> {

    LongHeader(String name) {
        super(Long.TYPE, name);
    }

    @Override
    public String toString(Long value) {
        return value.toString();
    }

    @Override
    public Long toValue(String value) {
        return Long.parseLong(value);
    }

}
