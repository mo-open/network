package org.mds.net.util;

import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Hashes passwords into hashes which can be tested for matching but which
 * the original password cannot be easily decrypted from.  The default
 * algorithm is SHA-512.
 *
 * @author
 */
@Singleton
public final class PasswordHasher {

    private final byte[] saltBytes;
    private final String algorithm;
    // A plokta def salt:
    static final String DEFAULT_SALT = "wlkefjasdfhadasdlkfjhwa,l.e,u.f,2.o3ads[]90as!!_$GHJM"
            + "<$^UJCMM<>OIUHGC^#YUJKTGYSUCINJd9f0awe0f9aefansjneaiw"
            + "aoeifa222222222222o(#(#(&@^!";
    private final Charset charset;
    public static final String SETTINGS_KEY_PASSWORD_SALT = "salt";
    public static final String SETTINGS_KEY_HASHING_ALGORITHM = "passwordHashingAlgorithm";
    public static final String DEFAULT_HASHING_ALGORITHM = "SHA-512";

    @Inject
    PasswordHasher(Settings settings, Charset charset) throws NoSuchAlgorithmException {
        this.charset = charset;
        String salt = settings.getString(SETTINGS_KEY_PASSWORD_SALT, DEFAULT_SALT);
        String alg = settings.getString(SETTINGS_KEY_HASHING_ALGORITHM, DEFAULT_HASHING_ALGORITHM);
        if (settings.getBoolean("productionMode", false) && DEFAULT_SALT.equals(salt)) {
            throw new ConfigurationError("Default password salt should not be used in "
                    + "production mode.  Set property salt for namespace timetracker to "
                    + "be something else");
        }
        saltBytes = salt.getBytes(charset);
        this.algorithm = alg;
        // fail early
        hash("abcd");
    }

    public boolean checkPassword(String unhashed, String hashed) {
        try {
            byte[] bytes = hash(unhashed);
            byte[] check = Base64.decodeBase64(hashed);
            return Arrays.equals(bytes, check);
        } catch (NoSuchAlgorithmException ex) {
            return Exceptions.chuck(ex);
        }
    }

    public String encryptPassword(String password) {
        try {
            return Base64.encodeBase64String(hash(password));
        } catch (NoSuchAlgorithmException ex) {
            return Exceptions.chuck(ex);
        }
    }

    private byte[] hash(String unhashed) throws NoSuchAlgorithmException {
        MessageDigest dg = MessageDigest.getInstance(algorithm);
        byte[] b = (unhashed + ":" + new String(saltBytes)).getBytes(charset);
        byte[] check = dg.digest(b);
        return check;
    }
}
