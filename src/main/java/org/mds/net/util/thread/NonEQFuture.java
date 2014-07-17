
package org.mds.net.util.thread;

import java.awt.EventQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A Future which enforces the threading model that it cannot be waited for on
 * the event thread.
 *
 * @author
 */
public class NonEQFuture<T> implements Future<T> {
    private final Future<T> delegate;

    public NonEQFuture(Future<T> delegate) {
        this.delegate = delegate;
    }

    public static <T> Future<T> wrap(Future<T> f) {
        if (f instanceof NonEQFuture) {
            return f;
        }
        return new NonEQFuture<T>(f);
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        checkThread();
        return delegate.get(timeout, unit);
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        checkThread();
        return delegate.get();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    private void checkThread() throws ExecutionException {
        if (EventQueue.isDispatchThread()) {
            throw new ExecutionException("Attempt to block on the event thread",
                    new IllegalThreadStateException());
        }
    }
}
