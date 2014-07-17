
package org.mds.net.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stub version of org.openide.util.Exceptions
 *
 * @author
 */
public final class Exceptions {
    private Exceptions() {}

    public static void printStackTrace(String msg, Throwable t) {
        Logger.getLogger(Exceptions.class.getName()).log(Level.SEVERE, msg, t);
    }

    public static void printStackTrace(Class<?> caller, String msg, Throwable t) {
        Logger.getLogger(caller.getName()).log(Level.SEVERE, msg, t);
    }

    public static void printStackTrace(Class<?> caller, Throwable t) {
        Logger.getLogger(caller.getName()).log(Level.SEVERE, null, t);
    }

    public static void printStackTrace(Throwable t) {
        Logger.getLogger(Exceptions.class.getName()).log(Level.SEVERE, null, t);
    }

    /**
     * Dirty trick to rethrow a checked exception.  Makes it possible to 
     * implement an interface such as Iterable (which cannot throw exceptions)
     * without the useless re-wrapping of exceptions in RuntimeException.
     * 
     * @param t A throwable.  This method will throw it without requiring a 
     * catch block.
     */
    public static <ReturnType> ReturnType chuck(Throwable t) {
        chuck(RuntimeException.class, t);
        throw new AssertionError(t); //should not get here
    }

    public static <T extends Throwable> void chuck(Class<T> type, Throwable t) throws T {
        throw (T) t;
    }
    
    @SuppressWarnings("ThrowableResultIgnored")
    public static <ReturnType> ReturnType chuckOriginal(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        chuck(RuntimeException.class, t);
        throw new AssertionError(t); //should not get here
    }
}
