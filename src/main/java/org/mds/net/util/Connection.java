
package org.mds.net.util;

/**
 *
 * @author
 */
public enum Connection {
    close, keep_alive;

    @Override
    public String toString() {
        switch (this) {
            case close:
                return name();
            case keep_alive:
                return "keep-alive";
            default:
                throw new AssertionError(this);
        }
    }
}
