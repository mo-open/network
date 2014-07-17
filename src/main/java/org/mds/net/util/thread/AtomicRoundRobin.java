package org.mds.net.util.thread;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Thread-safe, lockless AtomicInteger-like object whose value increments from 0 to
 * maximum-1 and returns to 0.
 *
 * @author
 */
public final class AtomicRoundRobin {
    private final int maximum;
    private volatile int currentValue;
    private final AtomicIntegerFieldUpdater<AtomicRoundRobin> up;
    public AtomicRoundRobin(int maximum) {
        if (maximum <= 0) {
            throw new IllegalArgumentException ("Maximum must be > 0");
        }
        this.maximum = maximum;
        up = AtomicIntegerFieldUpdater.newUpdater(AtomicRoundRobin.class, "currentValue");
    }

    /**
     * Get the maximum possible value
     * @return The maximum
     */
    public int maximum() {
        return maximum;
    }

    /**
     * Get the current value
     * @return
     */
    public int get() {
        return currentValue;
    }

    /**
     * Get the next value, incrementing the value or resetting it to zero
     * for the next caller.
     * @return The value
     */
    public int next() {
        if (maximum == 1) {
            return 0;
        }
        for (;;) {
            int current = get();
            int next = current == maximum - 1 ? 0 : current + 1;
            if (up.compareAndSet(this, current, next)) {
                return current;
            }
        }
    }
}
