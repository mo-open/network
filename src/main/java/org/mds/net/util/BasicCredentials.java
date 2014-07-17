
package org.mds.net.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author
 */
public class BasicCredentials {
    public final String username;
    public final String password;
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    public BasicCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    private static final Pattern HEADER = Pattern.compile("Basic (.*)");
    private static final Pattern UNPW = Pattern.compile("(.*?):(.*)");

    public static BasicCredentials parse(String header) {
        Matcher m = HEADER.matcher(header);
        if (m.matches()) {
            String base64 = m.group(1);
            byte[] bytes = base64.getBytes(US_ASCII);
            if (Base64.isArrayByteBase64(bytes)) {
                bytes = Base64.decodeBase64(bytes);
            }
            String s = new String(bytes, UTF_8);
            m = UNPW.matcher(s);
            if (m.matches()) {
                String username = m.group(1);
                String password = m.group(2);
                return new BasicCredentials(username, password);
            }
        }
        return null;
    }

    public String toString() {
        String merged = username + ':' + password;
        byte[] b = merged.getBytes(UTF_8);
        b = Base64.encodeBase64(b);
        return "Basic " + new String(b, US_ASCII);
    }

}
