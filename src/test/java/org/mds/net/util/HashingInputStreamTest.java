package org.mds.net.util;

import org.mds.net.util.streams.HashingInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class HashingInputStreamTest {

    @Test
    public void testMarkSupported() throws Throwable {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 256; i++) {
                out.write(i);
            }
        }
        byte[] bytes = out.toByteArray();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        HashingInputStream hin = HashingInputStream.sha1(in);

        ByteArrayOutputStream two = new ByteArrayOutputStream();

        int copied = Streams.copy(hin, two);
        assertEquals(bytes.length, copied);
        hin.close();

        byte[] nue = two.toByteArray();

        assertTrue(true);

        assertEquals(bytes, nue);

        assertNotNull(hin.getDigest());

        assertEquals(bytes.length, nue.length);

    }
}
