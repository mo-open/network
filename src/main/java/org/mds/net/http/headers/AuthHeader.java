
package org.mds.net.http.headers;

import org.mds.net.util.Realm;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * @author
 */
final class AuthHeader extends AbstractHeader<Realm> {

    AuthHeader() {
        super(Realm.class, HttpHeaders.Names.WWW_AUTHENTICATE);
    }

    @Override
    public String toString(Realm value) {
        return "Basic realm=\"" + value.toString() + "\"";
    }

    @Override
    public Realm toValue(String value) {
        return Realm.createSimple(value);
    }

}
