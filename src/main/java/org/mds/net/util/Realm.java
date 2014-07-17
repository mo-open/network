
package org.mds.net.util;

import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author
 */
public class Realm implements Comparable<Realm> {

    private final String name;

    @Inject
    protected Realm(@Named("realm") String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Realm && o.toString().equals(toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public int compareTo(Realm o) {
        return toString().compareTo(o.toString());
    }

    public static Realm createSimple(String name) {
        return new Realm(name);
    }
}
