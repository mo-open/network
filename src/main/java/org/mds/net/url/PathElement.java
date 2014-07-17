
package org.mds.net.url;

import org.mds.net.util.Checks;

/**
 * One element of a URL's path.
 *
 * @author
 */
public final class PathElement implements URLComponent {

    private static final long serialVersionUID = 1L;
    private final String element;
    private final boolean trailingSlash;
    private final boolean noEncode;

    public PathElement(String element) {
        this(element, false);
    }

    public PathElement(String element, boolean trailingSlash) {
        this(element, trailingSlash, false);
    }
    public PathElement(String element, boolean trailingSlash, boolean noEncode) {
        Checks.notNull("element", element);
        this.element = element;
        this.trailingSlash = trailingSlash;
        this.noEncode = noEncode;
    }

    String rawText() {
        return element;
    }

    PathElement toTrailingSlashElement() {
        return trailingSlash ? this : new PathElement(element, true, noEncode);
    }

    PathElement toNonTrailingSlashElement() {
        return trailingSlash ? new PathElement(element, false) : this;
    }

    @Override
    public boolean isValid() {
        return element.indexOf('/') < 0 && URLBuilder.isEncodableInLatin1(element);
    }

    @Override
    public String toString() {
        return noEncode ? element : URLBuilder.escape(element, '/', '+', ':', '?', '=');
    }

    @Override
    public String getComponentName() {
        return "path_element";
    }

    @Override
    public void appendTo(StringBuilder sb) {
        appendTo(sb, false);
    }

    public void appendTo(StringBuilder sb, boolean includeTrailingSlashIfPresent) {
        if (noEncode) {
            sb.append(element);
        } else {
            URLBuilder.append(sb, element, '/', '+', ':', '?', '=', '-');
        }
        if (trailingSlash && includeTrailingSlashIfPresent) {
            sb.append('/');
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
        final PathElement other = (PathElement) obj;
        if ((this.element == null) ? (other.element != null) : !this.element.equals(other.element)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.element != null ? this.element.hashCode() : 0);
        return hash;
    }

    public boolean isProbableFileReference() {
        return !trailingSlash && !"..".equals(element)
                && !".".equals(element) && element.indexOf('.') > 0;
    }
}
