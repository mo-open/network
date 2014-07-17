
package org.mds.net.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author tim
 */
public class GUIDFactory {

    private final Random r;
    private final char[] chars = 
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxqy0123456789".toCharArray();
    private static volatile GUIDFactory INSTANCE;

    // XXX use a more standard algorithm, maybe grab mac addresses
    public static GUIDFactory get() {
        if (INSTANCE == null) {
            synchronized (GUIDFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GUIDFactory();
                }
            }
        }
        return INSTANCE;
    }

    GUIDFactory() {
        //seed from a secure random, use a less expensive random to work with
        SecureRandom sr = new SecureRandom();
        shuffle(sr);
        r = new Random(sr.nextLong());
    }

    private void shuffle(SecureRandom r) {
        for (int i = 0; i < chars.length; i++) {
            int xa = r.nextInt(chars.length);
            char hold = chars[xa];
            chars[xa] = chars[i];
            chars[i] = hold;
        }
    }

    public String newGUID() {
        return newGUID(6, 5);
    }

    public String newGUID(int segments, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments; i++) {
            segment(length, sb);
            if (i != segments - 1) {
                sb.append('-');
            }
        }
        return sb.toString();
    }

    private void segment(int length, StringBuilder sb) {
        for (int i = 0; i < length; i++) {
            sb.append(chars[r.nextInt(chars.length)]);
        }
    }
}
