
package org.mds.net.util.thread;

/**
 * Simply overloads close() to not throw Exception
 *
 * @author
 */
public abstract class QuietAutoCloseable implements AutoCloseable {

    @Override
    public abstract void close();
}
