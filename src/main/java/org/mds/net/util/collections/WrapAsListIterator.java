
package org.mds.net.util.collections;

import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * @author
 */
final class WrapAsListIterator<T> implements ListIterator<T>{
    private final Iterator<T> iter;

    public WrapAsListIterator(Iterator<T> iter) {
        this.iter = iter;
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public T next() {
        return iter.next();
    }

    @Override
    public void remove() {
        iter.remove();
    }

    @Override
    public boolean hasPrevious() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T previous() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int nextIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int previousIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T e) {
        throw new UnsupportedOperationException();
    }
}
