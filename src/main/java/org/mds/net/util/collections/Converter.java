
package org.mds.net.util.collections;

/**
 * Converts an object from one type to another.
 *
 * @see ConvertList
 * @author
 */
public interface Converter<T, R> {

    public T convert(R r);

    public R unconvert(T t);
    
}
