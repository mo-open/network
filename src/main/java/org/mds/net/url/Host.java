
package org.mds.net.url;

import org.mds.net.util.AbstractBuilder;
import org.mds.net.util.Checks;

import java.util.Arrays;

/**
 * An internet host such as a host name or IP address.  Validation is
 * more extensive than that done by java.net.URL, including checking
 * name and label lengths for spec compatibility (max 63 chars per label,
 * 255 chars per host name).
 *
 * @author
 */
public final class Host implements URLComponent {
    private static final long serialVersionUID = 1L;
    private final Label[] labels;

    public Host(Label... domains) {
        Checks.notNull("domains", domains);
        Checks.noNullElements("domains", domains);
        this.labels = new Label[domains.length];
        System.arraycopy(domains, 0, this.labels, 0, domains.length);
    }

    public int size() {
        return labels.length;
    }

    public Label getElement(int ix) {
        return labels[ix];
    }

    public boolean isDomain(String domain) {
        Checks.notNull("domain", domain);
        Host host = Host.parse(domain);
        boolean result = true;
        int labelCount = host.size() - 1;
        int mySize = size() - 1;
        if (labelCount < 0 || mySize < 0) {
            return false;
        }
        do {
            result &= host.getElement(labelCount).equals(getElement(mySize));
            if (!result) {
                break;
            }
            labelCount--;
            mySize--;
        } while (labelCount >= 0 && mySize >= 0);
        return result;
    }

    public static Host parse(String path) {
        String[] parts = path.split("\\" + URLBuilder.LABEL_DELIMITER);
        Label[] els = new Label[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            els[i] = new Label(part);
        }
        return new Host(els);
    }

    public Label getTopLevelDomain() {
        if (isIpAddress()) {
            return null;
        }
        return labels.length > 1 ? labels[labels.length - 1] : null;
    }

    public Label getDomain() {
        if (isIpAddress()) {
            return null;
        }
        return labels.length > 1 ? labels[labels.length - 2] : null;
    }

    public Label[] getLabels() {
        Label[] result = new Label[labels.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = labels[labels.length - (1 + i)];
        }
        return result;
    }

    public Host getParentDomain() {
        if (labels.length > 1) {
            Label[] l = new Label[labels.length - 1];
            System.arraycopy(labels, 1, l, 0, l.length);
            return new Host(l);
        } else {
            return null;
        }
    }

    public boolean isIpAddress() {
        boolean result = labels.length > 0;
        if (result) {
            for (int i = 0; i < labels.length; i++) {
                result &= labels[i].isValidIpComponent();
            }
        }
        return result;
    }

    public int length() {
        int len = 0;
        for (Label dom : labels) {
            boolean hadContents = len > 0;
            if (hadContents) {
                len++;
            }
            len += dom.length();
        }
        return len;
    }

    @Override
    public boolean isValid() {
        if (isLocalhost() && !"".equals(toString())) {
            return true;
        }
        boolean ip = isIpAddress();
        boolean result = ip || (getTopLevelDomain() != null && getDomain() != null);
        if (result) {
            boolean someNumeric = false;
            boolean allNumeric = true;
            for (Label d : labels) {
                result &= d.isValid();
                boolean num = d.isNumeric();
                allNumeric &= num;
                someNumeric |= num;
            }
            if (someNumeric && !allNumeric) {
                return false;
            }
        }
        if (result) {
            int length = length();
            result = length <= 255 && length > 0;
        }
        if (result && ip) {
            int sz = size();
            result = sz == 4 || sz == 6;
        }
        return result;
    }


    public static AbstractBuilder<Label, Host> builder() {
        return new HostBuilder();
    }

    @Override
    public String getComponentName() {
        return "host";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < labels.length; i++) {
            if (sb.length() > 0) {
                sb.append(URLBuilder.LABEL_DELIMITER);
            }
            labels[i].appendTo(sb);
        }
        return sb.toString();
    }

    @Override
    public void appendTo(StringBuilder sb) {
        sb.append(toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Host other = (Host) obj;
        if (isLocalhost() && other.isLocalhost()) {
            return true;
        }
        if (!Arrays.equals(this.labels, other.labels)) {
            return false;
        }
        return true;
    }

    public boolean isLocalhost() {
        return (labels.length == 1 && "".equals(labels[0].toString())) || "127.0.0.1".equals(toString()) ||
                labels.length == 1 && "localhost".equals(labels[0].toString().toLowerCase());
    }

    @Override
    public int hashCode() {
        if (isLocalhost()) {
            return 1;
        }
        int hash = 5;
        hash = 73 * hash + Arrays.deepHashCode(this.labels);
        return hash;
    }

    private static final class HostBuilder extends AbstractBuilder<Label, Host> {

        @Override
        public Host create() {
            Label[] domains = elements().toArray(new Label[size()]);
            Label[] reversed = new Label[domains.length];
            for (int i = 0; i < domains.length; i++) {
                reversed[i] = domains[domains.length - (i + 1)];
            }
            return new Host(reversed);
        }

        @Override
        protected Label createElement(String string) {
            return new Label(string);
        }

    }
}
