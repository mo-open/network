
package org.mds.net.util.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Handles a few collections omissions
 *
 * @author
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * Create a reversed view of a list.  Unlike Collections.reverse() this does
     * not modify the original list;  it also does not copy the original list.
     * This does mean that modifications to the original list are visible while
     * iterating the child list, and appropriate steps should be taken to avoid
     * commodification.
     * 
     * @param <T> A type
     * @param list A list
     * @return A reversed view of the passed list
     */
    @SuppressWarnings("unchecked")
    public static<T> List<T> reversed(List<T> list) {
        if (list instanceof ReversedList) {
            return ((ReversedList<T>) list).delegate();
        }
        return new ReversedList<>(list);
    }

    /**
     * Create a list which wrappers another list and converts the contents on
     * demand
     *
     * @param <T> The old type
     * @param <R> The new type
     * @param list A list
     * @param converter A thing which converts between types
     * @param fromType The old type
     * @param toType The new type
     * @return A list
     */
    public static <T, R> List<R> convertedList(List<T> list, Converter<R, T> converter, Class<T> fromType, Class<R> toType) {
        return new ConvertList<>(toType, fromType, list, converter);
    }

    /**
     * Create an iterator which converts objects from one iterator into another
     * kind of object on the fly.
     *
     * @param c A thing that converts objects
     * @param iter An iterator
     * @return An iterator
     */
    public static <T, R> ListIterator<R> convertedIterator(Converter<R, T> c, Iterator<T> iter) {
        return convertedIterator(new WrapAsListIterator<>(iter), c);
    }

    /**
     * Covert a list iterator of one object type to another type of object
     *
     * @param iter An iterator
     * @param c The thing that converts objects
     * @return A list iterator
     */
    public static <T, R> ListIterator<R> convertedIterator(ListIterator<T> iter, Converter<R, T> c) {
        return new ConvertIterator<>(iter, c);
    }

    /**
     * Creates a list which uses object identity, not equals() to determine if
     * an object is a member or not, so methods such as remove() will only match
     * the same object even if two objects which equals() the object to remove
     * are available.
     * <p/>
     * The resulting List's own equals() and hashCode() methods are also
     * identity checks.
     */
    public static <T> List<T> identityList(Collection<T> collection) {
        return new IdentityList<>(collection);
    }

    /**
     * Create a List which uses identity comparisons to determine membership
     *
     * @param <T> A type
     * @return A lisst
     */
    public static <T> List<T> newIdentityList() {
        return new IdentityList<>();
    }

    /**
     * Invert a converter
     *
     * @param c A converter
     * @return A converter
     */
    public static <T, R> Converter<T, R> reverseConverter(Converter<R, T> c) {
        return new ReverseConverter(c);
    }

    /**
     * Wrap an iterator in JDK 6's Iterable
     *
     * @param iterator An iterator
     * @return An Iterable
     */
    public static <T> Iterable<T> toIterable(final Iterator<T> iterator) {
        return new IteratorIterable<>(iterator);
    }
    
    /**
     * Get an iterator which merges several iterators.
     *
     * @param <T> The type
     * @param iterators Some iterators
     * @return An iterator
     */
    public static <T> Iterator<T> combine(Collection<Iterator<T>> iterators) {
        return new MergeIterator<>(iterators);
    }

    /**
     * Combine two iterators
     *
     * @param <T> The type
     * @param a One iterator
     * @param b Another iterator
     * @return An iterator
     */
    public static <T> Iterator<T> combine(Iterator<T> a, Iterator<T> b) {
        return new MergeIterator<>(Arrays.asList(a, b));
    }
    
    public static <T> Iterator<T> singletonIterator(T obj) {
        return new SingletonIterator(obj);
    }

    private static final class MergeIterator<T> implements Iterator<T> {

        private final LinkedList<Iterator> iterators = new LinkedList<>();

        MergeIterator(Collection<Iterator<T>> iterators) {
            this.iterators.addAll(iterators);
        }

        private Iterator<T> iter() {
            if (iterators.isEmpty()) {
                return null;
            }
            Iterator result = iterators.get(0);
            if (!result.hasNext()) {
                iterators.remove(0);
                return iter();
            }
            return result;
        }

        @Override
        public boolean hasNext() {
            Iterator<T> curr = iter();
            return curr == null ? false : curr.hasNext();
        }

        @Override
        public T next() {
            Iterator<T> iter = iter();
            if (iter == null) {
                throw new NoSuchElementException();
            }
            return iter.next();
        }

        @Override
        public void remove() {
            Iterator<T> iter = iter();
            if (iter == null) {
                throw new NoSuchElementException();
            }
            iter.remove();
        }
    }    

    private static class IteratorIterable<T> implements Iterable<T> {

        private final Iterator<T> iter;

        public IteratorIterable(Iterator<T> iter) {
            this.iter = iter;
        }

        @Override
        public Iterator<T> iterator() {
            return iter;
        }
    }

    public static <T> Iterable<T> toIterable(final Enumeration<T> enumeration) {
        return new EnumIterable<>(enumeration);
    }

    private static final class EnumIterable<T> implements Iterable<T> {

        private final Enumeration<T> en;

        public EnumIterable(Enumeration<T> en) {
            this.en = en;
        }

        @Override
        public Iterator<T> iterator() {
            return toIterator(en);
        }
    }

    public static <T> Iterator<T> toIterator(final Enumeration<T> enumeration) {
        return new EnumIterator<>(enumeration);
    }

    private static final class EnumIterator<T> implements Iterator<T>, Iterable<T> {

        private final Enumeration<T> enumeration;

        public EnumIterator(Enumeration<T> enumeration) {
            this.enumeration = enumeration;
        }

        @Override
        public boolean hasNext() {
            return enumeration.hasMoreElements();
        }

        @Override
        public T next() {
            return enumeration.nextElement();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported");
        }

        @Override
        public Iterator<T> iterator() {
            return this;
        }
    }
    
    static class SingletonIterator<T> implements Iterator<T> {
        private final T object;
        private boolean done;
        public SingletonIterator(T object) {
            this.object = object;
        }

        @Override
        public boolean hasNext() {
            return !done;
        }

        @Override
        public T next() {
            if (done) {
                throw new NoSuchElementException();
            }
            done = true;
            return object;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
