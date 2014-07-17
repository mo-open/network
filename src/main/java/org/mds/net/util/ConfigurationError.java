
package org.mds.net.util;

/**
 * Error which can be thrown during startup/initialization, indicating
 * the application's configuration is so broken startup should be
 * aborted.  Thrown in cases such as a configuration parameter which must
 * be a number being a non-number string and that sort of thing.
 *
 * @author
 * @since 1.2.1
 */
public class ConfigurationError extends Error {

    public ConfigurationError() {
    }

    public ConfigurationError(String string) {
        super(string);
    }

    public ConfigurationError(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public ConfigurationError(Throwable thrwbl) {
        super(thrwbl);
    }

    public ConfigurationError(String string, Throwable thrwbl, boolean bln, boolean bln1) {
        super(string, thrwbl, bln, bln1);
    }
}
