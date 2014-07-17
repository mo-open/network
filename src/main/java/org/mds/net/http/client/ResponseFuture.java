
package org.mds.net.http.client;

import org.mds.net.util.Checks;
import org.mds.net.util.thread.Receiver;
import io.netty.channel.ChannelFuture;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.joda.time.DateTime;

/**
 * Returned from launching an HTTP request; attach handlers using the
 * <code>on(Class&lt;EventType&gt;, Receiver&lt;State&lt;T&gt;&gt;))</code>
 * method. Note that it is preferable to attach handlers when constructing the
 * request, unless you can guarantee that the request won't be completed before
 * your handler is attached.
 *
 * @author
 */
public final class ResponseFuture implements Comparable<ResponseFuture> {

    AtomicBoolean cancelled;
    final List<HandlerEntry<?>> handlers = new CopyOnWriteArrayList<>();
    final List<Receiver<State<?>>> any = new CopyOnWriteArrayList<>();
    private volatile ChannelFuture future;
    private final CountDownLatch latch = new CountDownLatch(1);
    private final DateTime start = DateTime.now();

    ResponseFuture(AtomicBoolean cancelled) {
        this.cancelled = cancelled;
    }

    void setFuture(ChannelFuture fut) {
        future = fut;
    }

    void trigger() {
        latch.countDown();
    }

    /**
     * Wait for the channel to be closed. Dangerous without a timeout!
     * <p/>
     * Note - blocking while waiting for a response defeats the purpose
     * of using an asynchronous HTTP client;  this sort of thing is
     * sometimes useful in unit tests, but should not be done in production
     * code.  Where possible, find a way to
     * attach a callback and finish work there, rather than use this
     * method.
     *
     * @throws InterruptedException
     */
    public ResponseFuture await() throws InterruptedException {
        latch.await();
        return this;
    }

    /**
     * Wait for a timeout for the request to be complleted. This is realy for
     * use in unit tests - normal users of this library should use callbacks.
     * <p/>
     * Note - blocking while waiting for a response defeats the purpose
     * of using an asynchronous HTTP client;  this sort of thing is
     * sometimes useful in unit tests, but should not be done in production
     * code.  Where possible, find a way to
     * attach a callback and finish work there, rather than use this
     * method.
     *
     * @param l A number of time units
     * @param tu Time units
     * @return follows the contract of CountDownLatch.await()
     * @throws InterruptedException
     */
    public ResponseFuture await(long l, TimeUnit tu) throws InterruptedException {
        Checks.notNull("tu", tu);
        Checks.nonNegative("l", l);
        latch.await(l, tu);
        return this;
    }

    /**
     * Cancel the associated request. This will make a best-effort, but cannot
     * guarantee, that no state changes will be fired after the final Cancelled.
     *
     * @return true if it succeeded, false if it was already canceled
     */
    public boolean cancel() {
        boolean result = cancelled.compareAndSet(false, true);
        if (result) {
            try {
                ChannelFuture fut = future;
                if (fut != null) {
                    fut.cancel(true);
                }
                if (fut.channel() != null && fut.channel().isOpen()) {
                    fut.channel().close();
                }
            } finally {
                event(new State.Cancelled());
            }
            latch.countDown();
        }
        return result;
    }

    private volatile Throwable error;

    /**
     * If an error was encountered, throw it
     *
     * @return this
     * @throws Throwable a throwable
     */
    public ResponseFuture throwIfError() throws Throwable {
        if (error != null) {
            throw error;
        }
        return this;
    }
    
    public final StateType lastState() {
        return lastState.get();
    }

    private AtomicReference<StateType> lastState = new AtomicReference<StateType>();
    <T> void event(State<T> state) {
        Checks.notNull("state", state);
        lastState.set(state.stateType());
        try {
            if (state instanceof State.Error && cancelled.get()) {
//                System.err.println("Suppressing error after cancel");
                return;
            }
            if (state instanceof State.Error) {
                error = ((State.Error) state).get();
            }
            for (HandlerEntry<?> h : handlers) {
                if (h.state.isInstance(state)) {
                    HandlerEntry<T> hh = (HandlerEntry<T>) h;
                    hh.onEvent(state);
                }
            }
            for (Receiver<State<?>> r : any) {
                r.receive(state);
            }
        } finally {
            if (state instanceof State.Closed) {
                latch.countDown();
            }
        }
    }

    /**
     * Add a listener which is notified of all state changes (there are many!).
     *
     * @param r
     * @return
     */
    public ResponseFuture onAnyEvent(Receiver<State<?>> r) {
        any.add(r);
        return this;
    }

    boolean has(Class<? extends State<?>> state) {
        if (!any.isEmpty()) {
            return true;
        }
        for (HandlerEntry<?> h : handlers) {
            if (state == h.state) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add a listener for a particular type of event
     */
    @SuppressWarnings("unchecked")
    public <T> ResponseFuture on(StateType state, Receiver<T> receiver) {
        StateType s = this.lastState.get();
        if (s == StateType.Closed && state == StateType.Closed) {
            receiver.receive(null);
        }
        Class<? extends State<T>> type = (Class<? extends State<T>>) state.type();
        return on(type, (Receiver<T>) state.wrapperReceiver(receiver));
    }

    public <T> ResponseFuture on(Class<? extends State<T>> state, Receiver<T> receiver) {
        HandlerEntry<T> handler = null;
        for (HandlerEntry<?> h : handlers) {
            if (state.equals(h.state)) {
                handler = (HandlerEntry<T>) h;
                break;
            }
        }
        if (handler == null) {
            handler = new HandlerEntry<>(state);
            handlers.add(handler);
        }
        handler.add(receiver);
        return this;
    }

    @Override
    public int compareTo(ResponseFuture t) {
        DateTime mine = this.start;
        DateTime other = t.start;
        return mine.compareTo(other);
    }
}
