
package org.mds.net.util;

/**
 * Special IllegalArgumentException for null parameters.  Checks.* use this
 * rather than NullPointerException to distinguish cases of code passing an illegal
 * null (clearly a bug on our part) from situations at runtime where something
 * is null which may not be in our code.
 *
 * @author
 */
public class NullArgumentException extends IllegalArgumentException {
    NullArgumentException (String msg) {
        super (msg);
    }
}
