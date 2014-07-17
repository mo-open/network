
package org.mds.net.http.headers;

/**
 *
 * @author
 */
abstract class AbstractHeader<T> implements HeaderValueType<T> {
    private final Class<T> type;
    private final String name;

    AbstractHeader(Class<T> type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<T> type() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HeaderValueType<?> && ((HeaderValueType<?>) obj).type() == type() 
                && ((HeaderValueType<?>) obj).name().equals(name());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.type.hashCode();
        hash = 79 * hash + this.name.hashCode();
        return hash;
    }

}
