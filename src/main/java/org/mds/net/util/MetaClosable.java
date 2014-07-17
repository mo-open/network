package org.mds.net.util;

import org.mds.net.util.thread.QuietAutoCloseable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper for multiple AutoCloseables.
 *
 * @author
 */
public class MetaClosable extends QuietAutoCloseable {

    private final AutoCloseable[] close;

    MetaClosable(AutoCloseable... close) {
        Checks.notEmptyOrNull("close", close);
        Checks.noNullElements("close", close);
        this.close = close;
    }

    private static MetaClosable of(AutoCloseable... closes) {
        return new MetaClosable(closes);
    }

    @Override
    public void close() {
        Exception ex = null;
        for (AutoCloseable cl : close) {
            try {
                cl.close();
            } catch (Exception e) {
                if (ex == null) {
                    ex = e;
                } else {
                    ex.addSuppressed(e);
                }
            }
        }
        if (ex != null) {
            Exceptions.chuck(ex);
        }
    }
}
