
package org.mds.net.util;

/**
 * Enum of valid values for cache control
 *
 * @author
 */
public enum CacheControlTypes {
    Public, Private, must_revalidate, proxy_revalidate, no_cache, no_store, 
    max_age(true), max_stale(true), min_fresh(true), 
    no_transform, only_if_cached;
    final boolean takesValue;

    private CacheControlTypes(boolean takesValue) {
        this.takesValue = takesValue;
    }

    CacheControlTypes() {
        this(false);
    }

    @Override
    public String toString() {
        char[] c = name().toLowerCase().toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '_') {
                c[i] = '-';
            }
        }
        return new String(c);
    }

    public static CacheControlTypes find(String s) {
        for (CacheControlTypes c : values()) {
            if (s.startsWith(c.toString())) {
                return c;
            }
        }
        return null;
    }
    
}
