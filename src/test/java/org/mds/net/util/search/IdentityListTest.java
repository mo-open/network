
package org.mds.net.util.search;

import org.mds.net.util.collections.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class IdentityListTest {

    @Test
    @SuppressWarnings("IncompatibleEquals")
    public void testIdList() {
        AlwaysEqual one = new AlwaysEqual();
        AlwaysEqual two = new AlwaysEqual();
        ArrayList<AlwaysEqual> al = new ArrayList<>();
        List<AlwaysEqual> il = CollectionUtils.newIdentityList();
        assertTrue(il.add(one));
        assertTrue(il.add(two));
        assertTrue(al.add(one));
        assertTrue(al.add(two));

        assertTrue(al.remove(new AlwaysEqual()));
        assertEquals(1, al.size());
        assertEquals(2, il.size());

        assertFalse(il.remove(new AlwaysEqual()));
        assertEquals(2, il.size());
        assertTrue(il.remove(two));
        assertEquals(1, il.size());
        assertTrue(il.remove(one));
        assertTrue(il.isEmpty());
        assertFalse(il.remove(one));

        assertEquals(il, il);
        assertFalse(il.equals(al));
    }

    static class AlwaysEqual {

        @Override
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        public boolean equals(Object o) {
            return true;
        }

        @Override
        public int hashCode() {
            return 23;
        }
    }
}
