
package org.mds.net.url;

import org.mds.net.util.Checks;


public class Anchor implements URLComponent {
    private static final long serialVersionUID = 1L;
    private final String anchor;

    public Anchor(String anchor) {
        Checks.notNull("anchor", anchor);
        this.anchor = anchor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(anchor.length() * 2);
        URLBuilder.append(sb, anchor);
        return sb.toString();
    }

    public boolean isValid() {
        return anchor.length() >= 0 && URLBuilder.isEncodableInLatin1(anchor);
    }

    public String getComponentName() {
        return "anchor";
    }

    public void appendTo(StringBuilder sb) {
        Checks.notNull("sb", sb);
        sb.append(URLBuilder.ANCHOR_DELIMITER);
        URLBuilder.append(sb, anchor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Anchor other = (Anchor) obj;
        if ((this.anchor == null) ? (other.anchor != null) : !this.anchor.equals(other.anchor)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.anchor != null ? this.anchor.hashCode() : 0);
        return hash;
    }
}
