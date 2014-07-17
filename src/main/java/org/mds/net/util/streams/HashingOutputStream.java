
package org.mds.net.util.streams;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.MessageDigest;

/**
 * Output stream which wraps another output stream and computes a cryptographic
 * hash using some algorithm as the data arrives.
 *
 * @author
 */
public final class HashingOutputStream extends FilterOutputStream {
    private final MessageDigest digest;
    private volatile boolean closed;
    public HashingOutputStream(String algorithm, OutputStream out) {
        super (out);
        digest = HashingInputStream.createDigest(algorithm);
    }

    public static HashingOutputStream sha1(OutputStream out) {
        return new HashingOutputStream("SHA-1", out);
    }

    @Override
    public void close() throws IOException {
        closed = true;
        super.close();
    }

    public byte[] getDigest() {
        if (!closed) {
            throw new IllegalStateException ("Stream not closed");
        }
        return digest.digest();
    }

    @Override
    public void write(int b) throws IOException {
        if (closed) {
            throw new IOException ("Stream closed");
        }
        super.write(b);
        digest.update((byte) b);
    }

    public String getHashAsString() throws IOException {
        if (!closed) {
            close();
        }
        byte[] bytes = getDigest();
        return hashString(bytes);
    }
    
    public static String hashString(byte[] bytes) {
        // assumes hash is a multiple of 4
        IntBuffer ib = ByteBuffer.wrap(bytes).asIntBuffer();
        StringBuilder sb = new StringBuilder();
        while (ib.position() < ib.capacity()) {
            long val = ib.get();
            if (val < 0) {
                val = -val + Integer.MAX_VALUE;
            }
            sb.append(Long.toString(val, 36));
        }
        return sb.toString();
    }
    
}
