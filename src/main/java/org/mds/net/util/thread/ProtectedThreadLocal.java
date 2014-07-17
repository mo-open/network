
package org.mds.net.util.thread;

/**
 *
 * @author
 */
public class ProtectedThreadLocal<T> {

    private final ThreadLocal<T> tl = new ThreadLocal<>();

    public QuietAutoCloseable set(T obj) {
        final T old = tl.get();
        tl.set(obj);
        return new TLAutoClose<>(tl, old);
    }

    private static final class TLAutoClose<T> extends QuietAutoCloseable {

        private final ThreadLocal<T> tl;
        private final T oldVal;

        private TLAutoClose(ThreadLocal<T> tl, T oldVal) {
            this.tl = tl;
            this.oldVal = oldVal;
        }

        @Override
        public void close() {
            if (oldVal == null) {
                tl.remove();
            } else {
                tl.set(oldVal);
            }
        }

        @Override
        public String toString() {
            return super.toString() + '[' + (tl.get() != null
                    ? tl.get() + "" : oldVal != null ? oldVal + "" : "null") + ']';
        }
    }

    public T get() {
        return tl.get();
    }

    public void remove() {
        tl.remove();
    }

    @Override
    public String toString() {
        T obj = get();
        return obj == null ? tl.toString() : obj + "";
    }
}
