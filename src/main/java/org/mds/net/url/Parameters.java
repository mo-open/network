
package org.mds.net.url;

import org.mds.net.util.AbstractBuilder;

/**
 *
 * @author
 */
public class Parameters implements URLComponent {
    private static final long serialVersionUID = 1L;
    private final String txt;
    Parameters(String s) {
        this.txt = s;
    }

    Parameters() {
        assert getClass() != Parameters.class;
        txt = null;
    }

    @Override
    public boolean isValid() {
        return URLBuilder.isEncodableInLatin1(txt);
    }

    @Override
    public String getComponentName() {
        return "parameters";
    }

    @Override
    public void appendTo(StringBuilder sb) {
        sb.append (txt);
    }

    public static AbstractBuilder<ParametersElement, Parameters> builder() {
        return ParsedParameters.builder();
    }

    public static Parameters parse (String params) {
        return ParsedParameters.parse(params);
    }

    public URLComponent[] getElements() {
        return new URLComponent[] { this };
    }
}
