
package org.mds.net.http.headers;

import org.mds.net.util.BasicCredentials;
import io.netty.handler.codec.http.HttpHeaders;

/**
 *
 * @author
 */
class BasicCredentialsHeader extends AbstractHeader<BasicCredentials> {

    BasicCredentialsHeader() {
        super(BasicCredentials.class, HttpHeaders.Names.AUTHORIZATION);
    }

    @Override
    public String toString(BasicCredentials value) {
        return value.toString();
    }

    @Override
    public BasicCredentials toValue(String value) {
        return BasicCredentials.parse(value);
    }

}
