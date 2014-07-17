
package org.mds.net.util.collections;

import java.util.ListIterator;

/**
 * A ListIterator which converts between types.  
 *
 * @author
 */
final class ConvertIterator<T, R> implements ListIterator<R> {
    private final ListIterator<T> orig;
    private final Converter<R,T> converter;
 
    public ConvertIterator(ListIterator<T> orig, Converter<R,T> converter) {
        this.orig = orig;
        this.converter = converter;
    }

    @Override
    public boolean hasNext() {
        return orig.hasNext();
    }

    @Override
    public R next() {
        return converter.convert(orig.next());
    }

    @Override
    public boolean hasPrevious() {
        return orig.hasPrevious();
    }

    @Override
    public R previous() {
        return converter.convert(orig.previous());
    }

    @Override
    public int nextIndex() {
        return orig.nextIndex();
    }

    @Override
    public int previousIndex() {
        return orig.previousIndex();
    }

    @Override
    public void remove() {
        orig.remove();
    }

    @Override
    public void set(R e) {
        orig.set(converter.unconvert(e));
    }

    @Override
    public void add(R e) {
        orig.add(converter.unconvert(e));
    }
}
