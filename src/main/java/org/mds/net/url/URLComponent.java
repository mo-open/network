
package org.mds.net.url;

import java.io.Serializable;

/**
 * One element of a URL, such as a Host, Port, Password, Path, PathElement,
 * Parameters or ParametersElement.
 *
 * @author
 */
public interface URLComponent extends Serializable {
    /**
     * Determine if this component is in compliance with the RFCs governing
     * URLs and DNS.
     * @return True if this component is valid.
     */
    public boolean isValid();
    /**
     * Get a human-readable, localized name for this part of the URL.
     * Useful if an error message needs to be shown.
     * @return The component name
     */
    public String getComponentName();
    /**
     * Append this component to a StringBuilder.
     * @param sb A StringBuilder
     */
    public void appendTo(StringBuilder sb);
}
