package org.mds.net.util;

import org.joda.time.Duration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Provides an authentication realm which changes every hour or whatever
 * interval is specified
 *
 * @author
 */
@Singleton
public final class RotatingRealmProvider implements Provider<Realm> {
    public static final String SETTINGS_KEY_ROTATE_INTERVAL_MINUTES = "realm.rotate.interval.minutes";
    public static final int DEFAULT_ROTATE_INTERVAL = 60;
    private final Duration interval;
    private final String salt;
    @Inject
    RotatingRealmProvider(Settings s, GUIDFactory randomStrings) {
        interval = Duration.standardMinutes(s.getInt(SETTINGS_KEY_ROTATE_INTERVAL_MINUTES, DEFAULT_ROTATE_INTERVAL));
        salt = randomStrings.newGUID(1, 4);
    }

    @Override
    public Realm get() {
        Duration dur = new Duration(System.currentTimeMillis());
        long minutesSince1970 = dur.toStandardMinutes().getMinutes();
        return new Realm(Long.toString(minutesSince1970 / interval.getStandardMinutes(), 36) + salt);
    }
}
