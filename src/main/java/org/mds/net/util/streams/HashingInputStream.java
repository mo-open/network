
package org.mds.net.util.streams;

import org.mds.net.util.Checks;
import org.mds.net.util.Exceptions;
import static org.mds.net.util.streams.HashingOutputStream.hashString;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Wrapper for an input stream which can compute a hash as bytes are read.
 *
 * @author
 */
public final class HashingInputStream extends FilterInputStream {

    private final InputStream wrapped;

    HashingInputStream(String algorithm, InputStream wrapped) {
        super(wrapped);
        Checks.notNull("wrapped", wrapped);
        Checks.notNull("algorithm", algorithm);
        this.wrapped = wrapped;
        digest = createDigest(algorithm);
    }
    private final MessageDigest digest;
    private volatile boolean closed;
    
    public static final MessageDigest createDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            return Exceptions.chuck(ex);
        }
    }

    public static HashingInputStream sha1(InputStream in) {
        return new HashingInputStream("SHA-1", in);
    }

    @Override
    public void close() throws IOException {
        closed = true;
        super.close();
    }

    public byte[] getDigest() {
        if (!closed) {
            throw new IllegalStateException("Stream not closed");
        }
        return digest.digest();
    }

    @Override
    public int read() throws IOException {
        int result = wrapped.read();
        digest.update((byte) result);
        return result;
    }

    public int read(byte b[], int off, int len) throws IOException {
        int result = wrapped.read(b, off, len);
        digest.update(b, off, len);
        return result;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public int available() throws IOException {
        return wrapped.available();
    }

    @Override
    public synchronized void mark(int readlimit) {
        wrapped.mark(readlimit);
    }

    @Override
    public boolean markSupported() {
        return wrapped.markSupported();
    }

    @Override
    public synchronized void reset() throws IOException {
        wrapped.reset();
    }
    
    public String getHashAsString() throws IOException {
        if (!closed) {
            close();
        }
        byte[] bytes = getDigest();
        return hashString(bytes);
    }    
}
