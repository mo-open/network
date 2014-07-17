
package org.mds.net.util.perf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for methods which will result in auto-generation of a JMX MBean
 * which provides access statistics for that method.
 * <p/>
 * Works with the JMX-AOP module, but is kept here so methods can be annotated
 * to be benchmarked while leaving the choice of including the JMX module 
 * optional.
 *
 * @author
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Benchmark {
    String value();

    Kind[] publish() default {};
    public enum Kind {
        CALL_COUNT('+'),
        TOTAL_TIME('=');
        private final char code;

        Kind(char code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "" + code;
        }
    }
}
