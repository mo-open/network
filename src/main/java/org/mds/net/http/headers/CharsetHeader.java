
package org.mds.net.http.headers;

import java.nio.charset.Charset;

/**
 *
 * @author
 */
class CharsetHeader extends AbstractHeader<Charset> {

    CharsetHeader(String name) {
        super(Charset.class, name);
    }

    @Override
    public String toString(Charset value) {
        return value.name();
    }

    @Override
    public Charset toValue(String value) {
        return Charset.forName(value);
    }

}
