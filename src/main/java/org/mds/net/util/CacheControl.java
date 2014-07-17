
package org.mds.net.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mds.net.util.CacheControlTypes.*;

/**
 *
 * @author
 */
public final class CacheControl {

    private final List<E> entries = new ArrayList<>();
    public static CacheControl PUBLIC_MUST_REVALIDATE
            = new CacheControl(Public, must_revalidate);
    public static CacheControl PUBLIC_MUST_REVALIDATE_MAX_AGE_1_DAY
            = new CacheControl(Public, must_revalidate).add(max_age, Duration.standardDays(1));
    public static CacheControl PRIVATE_NO_CACHE_NO_STORE 
            = new CacheControl(Private, no_cache, no_store);

    public CacheControl(CacheControlTypes... types) {
        for (CacheControlTypes c : types) {
            add(c);
        }
    }

    public static CacheControl $(CacheControlTypes types) {
        return new CacheControl(types);
    }
    private final DateTime creationTime = new DateTime();

    public boolean isExpired() {
        if (contains(CacheControlTypes.no_cache) || contains(CacheControlTypes.no_store)) {
            return true;
        }
        Long maxAgeSeconds = get(CacheControlTypes.max_age);
        if (maxAgeSeconds != null) {
            Duration dur = new Duration(new DateTime(), creationTime);
            Duration target = Duration.standardSeconds(maxAgeSeconds);
            if (dur.isLongerThan(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        for (E e : entries) {
            result += 79 * e.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CacheControl) {
            Set<E> a = new HashSet<>(entries);
            Set<E> b = new HashSet<>(((CacheControl) o).entries);
            return a.equals(b);
        }
        return false;
    }

    public CacheControl add(CacheControlTypes... types) {
        for (CacheControlTypes type : types) {
            _add(type);
        }
        return this;
    }

    public boolean contains(CacheControlTypes type) {
        for (E e : entries) {
            if (e.type == type) {
                return true;
            }
        }
        return false;
    }

    public long get(CacheControlTypes type) {
        if (!type.takesValue) {
            throw new IllegalArgumentException(type + " does not take a value");
        }
        for (E e : entries) {
            if (e.type == type) {
                return e.value;
            }
        }
        return -1;
    }

    void _add(CacheControlTypes type) {
        if (type.takesValue) {
            throw new IllegalArgumentException(type + " requires a value");
        }
        for (Iterator<E> it = entries.iterator(); it.hasNext();) {
            E e = it.next();
            if (e.type == type) {
                it.remove();
            }
        }
        entries.add(new E(type));
    }

    public CacheControl add(CacheControlTypes type, Duration value) {
        if (!type.takesValue) {
            throw new IllegalArgumentException(type + " requires a value");
        }
        for (Iterator<E> it = entries.iterator(); it.hasNext();) {
            E e = it.next();
            if (e.type == type) {
                it.remove();
            }
        }
        entries.add(new E(type, value.toStandardSeconds().getSeconds()));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<E> it = entries.iterator(); it.hasNext();) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public static CacheControl fromString(String s) {
        CacheControl result = new CacheControl();
        String[] parts = s.split(",");
        for (String part : parts) {
            part = part.trim();
            CacheControlTypes t = CacheControlTypes.find(part);
            if (t != null) {
                if (t.takesValue) {
                    String[] sides = part.split("=", 2);
                    if (sides.length == 2) {
                        try {
                            long val = Long.parseLong(sides[1]);
                            result.entries.add(new E(t, val));
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                            Logger.getLogger(CacheControl.class.getName()).log(Level.INFO, "Bad number in cache control header", nfe);
                        }
                    }
                } else {
                    result.add(t);
                }
            } else {
                System.err.println("Unrecognized: " + part);
            }
        }
        return result;
    }

    private static class E {

        private final CacheControlTypes type;
        private final long value;

        E(CacheControlTypes type) {
            this(type, -1);
        }

        E(CacheControlTypes type, long value) {
            this.type = type;
            this.value = value;
            assert type.takesValue || value == -1;
        }

        @Override
        public String toString() {
            if (type.takesValue) {
                return type + "=" + value;
            } else {
                return type.toString();
            }
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof E && ((E) o).type == type && (!type.takesValue || (((E) o).value == value));
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + this.type.hashCode();
            hash = 23 * hash + (int) (this.value ^ (this.value >>> 32));
            return hash;
        }
    }
}
