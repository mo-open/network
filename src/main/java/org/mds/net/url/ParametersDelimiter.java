
package org.mds.net.url;

/**
 * A query delimiter in a URL.
 *
 * @author
 */
public enum ParametersDelimiter {
    SEMICOLON(';'),
    AMPERSAND('&'),
    ;
    private char delimiter;
    ParametersDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public char charValue() {
        return delimiter;
    }
}
