
package org.mds.net.util.thread;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author
 */
public class LoggingExecutorService implements ExecutorService {

    private final ExecutorService svc;
    private final String name;

    LoggingExecutorService(ExecutorService svc) {
        this(svc.toString(), svc);
    }

    LoggingExecutorService(String name, ExecutorService svc) {
        this.svc = svc;
        this.name = name;
    }

    public static ExecutorService wrap(String name, ExecutorService svc) {
        if (svc instanceof LoggingExecutorService) {
            return svc;
        }
        return new LoggingExecutorService(name, svc);
    }

    public static ExecutorService wrap(ExecutorService svc) {
        if (svc instanceof LoggingExecutorService) {
            return svc;
        }
        return new LoggingExecutorService(svc);
    }
    
    @Override
    public String toString() {
        return "Wrapper for " + name + " (" + svc + ")";
    }

    @Override
    public void shutdown() {
        svc.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return svc.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return svc.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return svc.isTerminated();
    }

    @Override
    public boolean awaitTermination(long l, TimeUnit tu) throws InterruptedException {
        return svc.awaitTermination(l, tu);
    }

    @Override
    public <T> Future<T> submit(Callable<T> clbl) {
        System.out.println("Submit " + clbl);
        return svc.submit(clbl);
    }

    @Override
    public <T> Future<T> submit(Runnable r, T t) {
        System.out.println("Submit " + r);
        return svc.submit(r, t);
    }

    @Override
    public Future<?> submit(Runnable r) {
        System.out.println("Submit " + r);
        return svc.submit(r);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> clctn) throws InterruptedException {
        return svc.invokeAll(clctn);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> clctn, long l, TimeUnit tu) throws InterruptedException {
        return svc.invokeAll(clctn, l, tu);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> clctn) throws InterruptedException, ExecutionException {
        return svc.invokeAny(clctn);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> clctn, long l, TimeUnit tu) throws InterruptedException, ExecutionException, TimeoutException {
        return svc.invokeAny(clctn, l, tu);
    }

    @Override
    public void execute(Runnable r) {
        System.out.println("Execute " + r);
        try {
            svc.execute(r);
        } finally {
            System.out.println(" DONE: " + r);
        }
    }
}
