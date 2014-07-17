
package org.mds.net.http.headers;

/**
 *
 * @author
 */
//class HostHeader extends AbstractHeader<Host> {
class HostHeader extends AbstractHeader<String> {

    HostHeader(String name) {
        super(String.class, name);
    }

    @Override
//    public String toString(Host value) {
    public String toString(String value) {
        return value.toString();
    }

    @Override
//    public Host toValue(String value) {
    public String toValue(String value) {
        return value;
//        return Host.parse(value);
    }

}
