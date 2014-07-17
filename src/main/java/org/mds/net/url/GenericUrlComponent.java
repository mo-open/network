
package org.mds.net.url;

import org.mds.net.util.Checks;


/**
 * Url component with unspecified type.
 *
 * @author
 */
final class GenericUrlComponent implements URLComponent {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String value;
    private final boolean isAuthorityComponent;
    public GenericUrlComponent(String name, String value, boolean isAuthorityComponent) {
        Checks.notNull("name", name);
        Checks.notNull("value", value);
        this.name = name;
        this.value = value;
        this.isAuthorityComponent = isAuthorityComponent;
    }

    public boolean isValid() {
        boolean result = value != null;
        if (result) {
            for (char c : value.toCharArray()) {
                if (isAuthorityComponent) {
                    if (!URLBuilder.isLetter(c) && !URLBuilder.isNumber(c) && c != '.' && c != '-') {
                        return false;
                    }
                }
            }
        }
        return result;
    }

    public String getComponentName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendTo(sb);
        return sb.toString();
    }

    public void appendTo(StringBuilder sb) {
        Checks.notNull("sb", sb);
        if (isAuthorityComponent) {
            sb.append(value);
        } else {
            URLBuilder.append(sb, value);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GenericUrlComponent other = (GenericUrlComponent) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
            return false;
        }
        if (this.isAuthorityComponent != other.isAuthorityComponent) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 19 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 19 * hash + (this.isAuthorityComponent ? 1 : 0);
        return hash;
    }

}
