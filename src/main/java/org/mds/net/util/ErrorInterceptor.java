package org.mds.net.util;

/**
 * Injectable interceptor for exceptions thrown.  Used in tests which want
 * to fail if server-side exceptions occur.
 *
 * @author
 */
public interface ErrorInterceptor {
    public void onError(Throwable err);
}
