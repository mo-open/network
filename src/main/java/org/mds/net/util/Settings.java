package org.mds.net.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;


public interface Settings extends Iterable<String> {

    public Integer getInt(String name);

    public int getInt(String name, int defaultValue);

    public Long getLong(String name);

    public long getLong(String name, long defaultValue);

    public String getString(String name);

    public String getString(String name, String defaultValue);

    public Boolean getBoolean(String name);

    public boolean getBoolean(String name, boolean defaultValue);

    public Double getDouble(String name);

    public double getDouble(String name, double defaultValue);

    public Set<String> allKeys();

    /**
     * Get this settings as a read-only Properties object
     *
     * @return
     */
    public Properties toProperties();

    public Settings EMPTY = new Settings() {

        @Override
        public Integer getInt(String name) {
            return null;
        }

        @Override
        public int getInt(String name, int defaultValue) {
            return defaultValue;
        }

        @Override
        public Long getLong(String name) {
            return null;
        }

        @Override
        public long getLong(String name, long defaultValue) {
            return defaultValue;
        }

        @Override
        public String getString(String name) {
            return null;
        }

        @Override
        public String getString(String name, String defaultValue) {
            return defaultValue;
        }

        @Override
        public Boolean getBoolean(String name) {
            return null;
        }

        @Override
        public boolean getBoolean(String name, boolean defaultValue) {
            return defaultValue;
        }

        @Override
        public Double getDouble(String name) {
            return null;
        }

        @Override
        public double getDouble(String name, double defaultValue) {
            return defaultValue;
        }

        @Override
        public Set<String> allKeys() {
            return Collections.<String>emptySet();
        }

        @Override
        public Properties toProperties() {
            return new Properties();
        }

        @Override
        public Iterator<String> iterator() {
            return allKeys().iterator();
        }

    };
}