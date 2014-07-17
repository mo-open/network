
package org.mds.net.http.headers;

import java.util.Locale;

/**
 *
 * @author
 */
class LocaleHeader extends AbstractHeader<Locale> {

    LocaleHeader(String name) {
        super(Locale.class, name);
    }

    @Override
    public String toString(Locale value) {
        return value.toLanguageTag();
    }

    @Override
    public Locale toValue(String value) {
        return Locale.forLanguageTag(value);
    }

}
