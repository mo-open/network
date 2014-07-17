
package org.mds.net.url;

import org.mds.net.util.AbstractBuilder;
import org.mds.net.util.Checks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The Parameters portion of a URL, e.g.<br/>
 * <code>http://timboudreau.com/foo.html<b>?foo=bar&amp;bar=baz</b>#something</code>
 *
 * @author
 */
final class ParsedParameters extends Parameters implements URLComponent {
    private static final long serialVersionUID = 1L;
    private final ParametersElement[] elements;
    private final ParametersDelimiter delimiter;

    public ParsedParameters(ParametersElement... elements) {
        this(ParametersDelimiter.AMPERSAND, elements);
    }

    public ParsedParameters(ParametersDelimiter delimiter, ParametersElement... elements) {
        Checks.notNull("elements", elements);
        Checks.notNull("delimiter", delimiter);
        Checks.noNullElements("elements", elements);
        this.delimiter = delimiter;
        this.elements = new ParametersElement[elements.length];
        System.arraycopy(elements, 0, this.elements, 0, elements.length);
    }

    public ParametersDelimiter getDelimiter() {
        return delimiter;
    }

    public static Parameters parse(String queryString) {
        Checks.notNull("queryString", queryString);
        if (queryString == null || queryString.length() == 0) {
            return null;
        }
        String[] parts = null;
        ParametersDelimiter delim = ParametersDelimiter.AMPERSAND;
        if (queryString.indexOf(ParametersDelimiter.AMPERSAND.charValue()) >= 0) {
            parts = queryString.split("\\" + ParametersDelimiter.SEMICOLON.charValue());
            delim = ParametersDelimiter.AMPERSAND;
        } else if (queryString.indexOf(ParametersDelimiter.SEMICOLON.charValue()) >= 0) {
            parts = queryString.split("\\" + ParametersDelimiter.SEMICOLON.charValue());
            delim = ParametersDelimiter.SEMICOLON;
        } else {
            if (queryString.startsWith("=") && queryString.length() > 1) {
                ParsedParameters result = new ParsedParameters(ParametersDelimiter.AMPERSAND, new ParametersElement(null, queryString.substring(1)));
                return result;
            } else if (queryString.startsWith("?=") && queryString.length() > 2) {
                return new ParsedParameters(ParametersDelimiter.AMPERSAND, new ParametersElement(null, queryString.substring(2)));
            } else {
                return new ParsedParameters(ParametersDelimiter.AMPERSAND, new ParametersElement(queryString, null));
            }
        }
        ParametersElement[] els = new ParametersElement[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            els[i] = ParametersElement.parse(part);
        }
        ParsedParameters result = new ParsedParameters(delim, els);
        if (!result.unescaped().equals('?' + queryString)) {
            return new Parameters(queryString);
        }
        return result;
    }

    public ParametersElement[] getElements() {
        ParametersElement[] result = new ParametersElement[elements.length];
        System.arraycopy(elements, 0, result, 0, elements.length);
        return result;
    }

    public int size() {
        return elements.length;
    }

    public String unescaped() {
        String s = toString(delimiter);
        return URLBuilder.unescape(s);
    }

    @Override
    public String toString() {
        return toString(delimiter);
    }

    public String toString(ParametersDelimiter delimiter) {
        Checks.notNull("delimiter", delimiter);
        if (elements.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        appendTo(sb, delimiter);
        return sb.toString();
    }

    public boolean isValid() {
        for (ParametersElement el : elements) {
            if (!el.isValid()) {
                return false;
            }
        }
        return true;
    }

    public String getComponentName() {
        return "parameters";
    }

    public void appendTo(StringBuilder sb) {
        Checks.notNull("sb", sb);
        appendTo(sb, getDelimiter());
    }

    public void appendTo(StringBuilder sb, ParametersDelimiter delimiter) {
        Checks.notNull("delimiter", delimiter);
        Checks.notNull("sb", sb);
        sb.append(URLBuilder.QUERY_PREFIX);
        int max = elements.length;
        for (int i = 0; i < max; i++) {
            if (i > 0) {
                sb.append(delimiter.charValue());
            }
//            sb.append (elements[i]);
            if (elements[i] == ParametersElement.EMPTY) {
                sb.append(delimiter.charValue());
            } else {
                elements[i].appendTo(sb);
            }
        }
    }

    /**
     * Create a builder to which <a href="ParametersElement.html"><code>ParametersElement</code></a>s
     * may be added conveniently,
     * to construct a Parameters object.
     *
     * @return a builder for <code>Parameters</code> objects
     */
    public static AbstractBuilder<ParametersElement, Parameters> builder() {
        return new QueryBuilder();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParsedParameters other = (ParsedParameters) obj;
        if (!Arrays.deepEquals(this.elements, other.elements)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Arrays.deepHashCode(this.elements);
        return hash;
    }

    private static class QueryBuilder extends AbstractBuilder<ParametersElement, Parameters> {

        @Override
        public Parameters create() {
            List<ParametersElement> l = elements();
            Collections.sort(l);
            ParametersElement[] qe = l.toArray(new ParametersElement[l.size()]);
            return new ParsedParameters(qe);
        }

        @Override
        protected ParametersElement createElement(String string) {
            int ix = string.indexOf('=');
            if (ix > 0) {
                String key = string.substring(0, ix);
                String value = string.substring(ix);
                return new ParametersElement(key, value);
            } else if (ix == 0) {
                return new ParametersElement(null, string.substring(1));
            } else if (ix < 0) {
                return new ParametersElement(string, null);
            }
            return null;
        }

    }
}
