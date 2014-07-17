
package org.mds.net.util;

import org.mds.net.util.thread.Receiver;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author
 */
public class Types {

    private Types() {
    }

    public static void visit(Class<?> type, Receiver<Class<?>> r) {
        Set<Class<?>> visited = new HashSet<>();
        while (type != null && type != Object.class) {
            r.receive(type);
            for (Class<?> c : type.getInterfaces()) {
                if (!visited.contains(c)) {
                    visited.add(c);
                    r.receive(c);
                }
                visited.add(c);
            }
            visited.add(type);
            type = type.getSuperclass();
        }
    }
    
    public static Set<Class<?>> get(Class<?> type) {
        final Set<Class<?>> result = new HashSet<>();
        visit(type, new Receiver<Class<?>> (){

            @Override
            public void receive(Class<?> object) {
                result.add(object);
            }
        });
        if (Object.class.isAssignableFrom(type)) {
            result.add(Object.class);
        }
        return result;
    }
    
    public static String list(Class<?> type) {
        Set<Class<?>> c = get(type);
        StringBuilder sb = new StringBuilder();
        for (Iterator<Class<?>> it = c.iterator(); it.hasNext();) {
        Class<?> cc = it.next();
            sb.append(cc.getSimpleName());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
