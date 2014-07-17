
package org.mds.net.url;


/**
 * A TCP port.  Represents the optional port portion of a URL, such as
 * <code>https://www.timboudreau.com:<b>8443</b>/management/</code>
 *
 * @author
 */
public final class Port implements URLComponent {
    private static final long serialVersionUID = 1L;
    private String port;
    private boolean valid;
    public Port (int port) {
        this.port = Integer.toString(port);
        valid = true;
    }

    public Port (String port) {
        try {
            this.port = Integer.toString(Integer.parseInt(port));
            valid = true;
        } catch (NumberFormatException nfe) {
            this.port = port;
            valid = false;
        }
    }

    boolean isIllegalChars() {
        return !valid;
    }

    /**
     * Get the integer value of this port
     * @return
     */
    public int intValue() {
        return valid ? Integer.parseInt(port) : -1;
    }

    @Override
    public String toString() {
        return port;
    }

    /**
     * Determine if this is a valid port according to specification (i.e.
     * port is non-negative and value is between 1 and 65535).
     * @return
     */
    public boolean isValid() {
        if (valid) {
            int val = intValue();
            return val > 0 && val < 65536;
        }
        return false;
    }

    @Override
    public String getComponentName() {
        return "port";
    }

    @Override
    public void appendTo(StringBuilder sb) {
        sb.append (port);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Port && (((Port) obj).port == null ? port == null : ((Port) obj).port.equals(port));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
