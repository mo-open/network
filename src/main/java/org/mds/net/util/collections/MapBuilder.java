package org.mds.net.util.collections;

import org.mds.net.util.Exceptions;
import org.mds.net.util.streams.HashingOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A map builder with a fluent API, ability to convert to ordered Properties and ability
 * to compute a hash using a message digest.
 *
 * @author
 */
public final class MapBuilder {

    final Map<String, Object> data = new LinkedHashMap<>();
    private final MessageDigest digest;

    public MapBuilder(MessageDigest digest) {
        this.digest = digest;
    }

    public MapBuilder() {
        this(null);
    }

    public String hash() {
        return digest == null ? null : 
                HashingOutputStream.hashString(digest.digest());
    }

    public MapBuilder subMap(String name) {
        MapBuilder nue = new MapBuilder(digest);
        data.put(name, nue.data);
        if (digest != null) {
            try {
                digest.update(name.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Exceptions.chuck(ex);
            }
            digest.update((byte) '!');
        }
        return nue;
    }

    public MapBuilder put(String name, Object type) {
        data.put(name, type);
        if (digest != null) {
            try {
                digest.update(name.getBytes("UTF-8"));
                digest.update((byte) '=');
                digest.update(("" + type).getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                Exceptions.chuck(ex);
            }
        }
        return this;
    }

    public Properties toProperties() {
        Properties props = new Properties();
        for (Map.Entry<String, Object> e : data.entrySet()) {
            if (e.getValue() instanceof String) {
                props.setProperty(e.getKey(), e.getValue().toString());
            }
        }
        return props;
    }
    
    public Map<String,Object> build() {
        return new LinkedHashMap<>(data);
    }
}
