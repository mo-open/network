
package org.mds.net.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author
 */
final class ByteBufferCollectionInputStream extends InputStream {

    private final Iterator<ByteBuffer> iter;
    private int count;

    ByteBufferCollectionInputStream(ByteBuffer... buffers) {
        this(Arrays.asList(buffers));
    }

    ByteBufferCollectionInputStream(Iterable<ByteBuffer> ib) {
        iter = ib.iterator();
        if (ib instanceof Collection) {
            count = ((Collection)ib).size();
        }
    }
    private ByteBuffer buf;

    private ByteBuffer buf() {
        if (buf == null) {
            if (iter.hasNext()) {
                buf = iter.next();
                if (buf.position() != 0) {
                    buf.flip();
                }
                return buf;
            }
        }
        if (buf != null) {
            if (buf.position() == buf.limit()) {
                buf = null;
                return buf();
            }
        }
        return buf;
    }

    @Override
    public int read() throws IOException {
        ByteBuffer b = curr();
        if (b == null) {
            return -1;
        }
        return b.get();
    }

    ByteBuffer curr() {
        ByteBuffer b = buf();
        if (b == null) {
            return null;
        }
        while (b != null && b.position() == b.limit()) {
            b = buf();
        }
        return b;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        ByteBuffer b = curr();
        if (b == null) {
            return -1;
        }
        int oldPos = b.position();
        b.get(bytes);
        return b.position() - oldPos;
    }

    @Override
    public int read(byte[] bytes, int offset, int len) throws IOException {
        ByteBuffer b = curr();
        if (b == null) {
            return -1;
        }
        int oldPos = b.position();
        int remaining = b.limit() - oldPos;
        if (remaining > 0) {
            b.get(bytes, offset, Math.min(remaining, len));
        }
        return b.position() - oldPos;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + " with " + count + " buffers finished?" + iter.hasNext();
    }
}
