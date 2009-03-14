/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/merge.html
 */
public class MergeTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testMerge() throws IOException {
        YamlDocument document = new YamlDocument("types/merge.yaml");
        List list = (List) document.getNativeData();
        assertEquals(8, list.size());
        Map<Object, Object> center = (Map<Object, Object>) list.get(0);
        assertEquals(2, center.size());
        assertEquals(new Integer(1), center.get("x"));
        assertEquals(new Integer(2), center.get("y"));
        //
        Map<Object, Object> left = (Map<Object, Object>) list.get(1);
        assertEquals(2, left.size());
        assertEquals(left.get("x").getClass().toString(), new Integer(0), left.get("x"));
        assertEquals(new Integer(2), left.get("y"));
        //
        Map<Object, Object> big = (Map<Object, Object>) list.get(2);
        assertEquals(1, big.size());
        assertEquals(new Integer(10), big.get("r"));
        //
        Map<Object, Object> small = (Map<Object, Object>) list.get(3);
        assertEquals(1, small.size());
        assertEquals(new Integer(1), small.get("r"));
        // Explicit keys
        Map<Object, Object> explicit = (Map<Object, Object>) list.get(4);
        assertEquals(4, explicit.size());
        assertEquals(new Integer(1), explicit.get("x"));
        assertEquals(new Integer(2), explicit.get("y"));
        assertEquals(new Integer(10), explicit.get("r"));
        assertEquals("center/big", explicit.get("label"));
        // Merge one map
        Map<Object, Object> merged1 = (Map<Object, Object>) list.get(5);
        assertEquals(explicit, merged1);
        assertNotSame(explicit, merged1);
        // Merge multiple maps
        Map<Object, Object> merged2 = (Map<Object, Object>) list.get(6);
        assertEquals(explicit, merged2);
        assertNotSame(explicit, merged2);
        // Override
        Map<Object, Object> merged3 = (Map<Object, Object>) list.get(7);
        assertEquals(explicit, merged3);
        assertNotSame(explicit, merged3);
    }
}
