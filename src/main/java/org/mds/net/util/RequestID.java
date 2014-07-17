
package org.mds.net.util;

import org.joda.time.Duration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unique ID for an HTTP request which can be injected
 *
 * @author
 */
public final class RequestID {

    private static final AtomicInteger indexSource = new AtomicInteger();
    public final int index = indexSource.getAndIncrement();
    public final long time = System.currentTimeMillis();

    public int getIndex() {
        return index;
    }

    public Duration getDuration() {
        return new Duration(System.currentTimeMillis() - time);
    }

    @Override
    public String toString() {
        return index + "/" + getDuration().getMillis() + "ms";
    }
}
