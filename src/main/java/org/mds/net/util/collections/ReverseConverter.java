
package org.mds.net.util.collections;

/**
 *
 * @author
 */
final class ReverseConverter<T, R> implements Converter<T, R> {
    private final Converter<R, T> delegate;

    ReverseConverter(Converter<R, T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T convert(R r) {
        return delegate.unconvert(r);
    }

    @Override
    public R unconvert(T t) {
        return delegate.convert(t);
    }
}
