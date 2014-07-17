
package org.mds.net.url;

/**
 * The protocol portion of a URL (e.g. "http").  See the enum
 * <a href="Protocols.html"><code>Protocols</code></a> for standard
 * protocols.
 * <p/>
 * Note:  Any external implementation of this class should implement equals and
 * hash code test equality of getName() without case-sensitivity.
 * <p/>
 *
 * @author
 */
public interface Protocol extends URLComponent {
    public String getName();
    public Port getDefaultPort();
    public boolean isSecure();
    public boolean match(String protocol);
    public boolean isKnownProtocol();
    public boolean isNetworkProtocol();
    public boolean isSecureVersionOf(Protocol other);
    public boolean isInsecureVersionOf(Protocol other);
    public static Protocol INVALID = new Protocol() {
        private static final long serialVersionUID = 1L;
            
        public String getName() {
            return "invalid_protocol";
        }

        public Port getDefaultPort() {
            return new Port(0);
        }

        public boolean isSecure() {
            return false;
        }

        public boolean match(String protocol) {
            return getName().equals(protocol);
        }

        public boolean isKnownProtocol() {
            return false;
        }

        public boolean isValid() {
            return false;
        }

        public String getComponentName() {
            return Protocols.HTTP.getComponentName();
        }

        public void appendTo(StringBuilder sb) {
            sb.append ("invalid");
        }

        public boolean isNetworkProtocol() {
            return true;
        }

        public boolean isSecureVersionOf(Protocol other) {
            return false;
        }

        public boolean isInsecureVersionOf(Protocol other) {
            return false;
        }
    };
}
