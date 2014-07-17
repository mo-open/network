
package org.mds.net.util;

import java.io.IOException;

/**
 * A server which can be started, waited on and shutdown.
 *
 * @author
 */

public interface Server {
    
    /**
     * Get the default port this server will run on, or if multiple instances
     * have been started on different ports, the last-used port.  For accurate
     * values, use ServerControl.getPort() after calling start().
     * @return The port
     */
    int getPort();

    /**
     * Start the server.
     * @return A ServerControl object which can be waited on for the server
     * to be shut down, and whose signal()/signalAll() methods will trigger
     * a shutdown
     * @throws java.io.IOException if something goes wrong
     */
    ServerControl start() throws IOException;
    /**
     * Start the server on a specific port.
     *
     * @param port The port
     * @return A ServerControl object which can be waited on for the server
     * to be shut down, and whose signal()/signalAll() methods will trigger
     * a shutdown
     * @throws java.io.IOException
     */
    ServerControl start(int port) throws IOException;

    /**
     * Start the server.
     * @return A ServerControl object which can be waited on for the server
     * to be shut down, and whose signal()/signalAll() methods will trigger
     * a shutdown
     * @throws java.io.IOException if something goes wrong
     */
    ServerControl start(boolean ssl) throws IOException;
    /**
     * Start the server on a specific port.
     *
     * @param port The port
     * @return A ServerControl object which can be waited on for the server
     * to be shut down, and whose signal()/signalAll() methods will trigger
     * a shutdown
     * @throws java.io.IOException
     */
    ServerControl start(int port, boolean ssl) throws IOException;
    
}
