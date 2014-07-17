package org.mds.net.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author
 */
public interface Codec {

    public <T> String writeValueAsString(T object) throws IOException;

    public <T> byte[] writeValueAsBytes(T object, OutputStream out) throws IOException;

    public <T> T readValue(InputStream byteBufInputStream, Class<T> type) throws IOException;
}
