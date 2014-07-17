
package org.mds.net.http.headers;

import org.joda.time.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 */
final class DurationHeader extends AbstractHeader<Duration> {

    DurationHeader(String name) {
        super(Duration.class, name);
    }

    @Override
    public String toString(Duration value) {
        return value.getStandardSeconds() + "";
    }

    @Override
    public Duration toValue(String value) {
        try {
            return new Duration(Long.parseLong(value));
        } catch (NumberFormatException nfe) {
            Logger.getLogger(DurationHeader.class.getName()).log(Level.INFO, "Bad duration header '" + value + "'", nfe);
            return null;
        }
    }

}
