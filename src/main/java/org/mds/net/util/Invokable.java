
package org.mds.net.util;

import java.util.concurrent.Callable;

/**
 * Callable-like construct which throws a typed exception and takes an argument.
 *
 * @author
 */
public abstract class Invokable<ArgType, ResultType, ExceptionType extends Exception> {

    private final Class<ExceptionType> thrown;

    protected Invokable(Class<ExceptionType> thrown) {
        this.thrown = thrown;
    }

    public static Invokable<?, ?, RuntimeException> wrap(final Runnable r) {
        return new Invokable<Void, Void, RuntimeException>() {
            @Override
            public Void run(Void argument) throws RuntimeException {
                r.run();
                return null;
            }
        };
    }

    @SuppressWarnings("unchecked")
    protected Invokable() {
        //Have to bypass generics for this
        Class c = RuntimeException.class;
        this.thrown = c;
    }

    public abstract ResultType run(ArgType argument) throws ExceptionType;

    public final Class<ExceptionType> thrown() {
        return thrown;
    }

    public Callable<ResultType> toCallable() {
        return new Callable<ResultType>() {
            @Override
            public ResultType call() throws Exception {
                return run(null);
            }
        };
    }

    public Callable<ResultType> toCallable(final ArgType arg) {
        return new Callable<ResultType>() {
            @Override
            public ResultType call() throws Exception {
                return run(arg);
            }
        };
    }

    public Runnable toRunnable(final ArgType arg) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Invokable.this.run(arg);
                } catch (Exception ex) {
                    Exceptions.chuck(ex);
                }
            }
        };
    }
}
