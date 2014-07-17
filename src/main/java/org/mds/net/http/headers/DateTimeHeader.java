
package org.mds.net.http.headers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;

/**
 *
 * @author
 */
class DateTimeHeader extends AbstractHeader<DateTime> {

    DateTimeHeader(String name) {
        super(DateTime.class, name);
    }

    @Override
    public String toString(DateTime value) {
        return Headers.toISO2822Date(value);
    }

    @Override
    public DateTime toValue(String value) {
        long val = 0;
        if (val == 0) {
            try {
                val = Headers.ISO2822DateFormat.parseDateTime(value).getMillis();
            } catch (IllegalArgumentException e) {
                try {
                    //Sigh...use java.util.date to handle "GMT", "PST", "EST"
                    val = Date.parse(value);
                } catch (IllegalArgumentException ex) {
                    new IllegalArgumentException(value, ex).printStackTrace();
                    return null;
                }
            }
        }
        DateTime result = new DateTime(val, DateTimeZone.UTC);
        //to be truly compliant, accept 2-digit dates
        if (result.getYear() < 100 && result.getYear() > 0) {
            if (result.getYear() >= 50) {
                result = result.withYear(2000 - (100 - result.getYear())).withDayOfYear(result.getDayOfYear() - 1); //don't ask
            } else {
                result = result.withYear(2000 + result.getYear());
            }
        }
        return result;
    }

}
