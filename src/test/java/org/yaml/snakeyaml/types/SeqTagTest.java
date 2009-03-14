/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/seq.html
 */
public class SeqTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testSeq() {
        YamlDocument document = new YamlDocument("types/seq.yaml");
        Map<String, List<String>> map = (Map<String, List<String>>) document.getNativeData();
        assertEquals(2, map.size());
        List<String> list1 = (List<String>) map.get("Block style");
        assertEquals(9, list1.size());
        assertEquals("Mercury", list1.get(0));
        assertEquals("Venus", list1.get(1));
        assertEquals("Earth", list1.get(2));
        assertEquals("Mars", list1.get(3));
        assertEquals("Jupiter", list1.get(4));
        assertEquals("Saturn", list1.get(5));
        assertEquals("Uranus", list1.get(6));
        assertEquals("Neptune", list1.get(7));
        assertEquals("Pluto", list1.get(8));
        //
        List<String> list2 = (List<String>) map.get("Flow style");
        assertEquals(9, list2.size());
        assertEquals("Mercury", list2.get(0));
        assertEquals("Venus", list2.get(1));
        assertEquals("Earth", list2.get(2));
        assertEquals("Mars", list2.get(3));
        assertEquals("Jupiter", list2.get(4));
        assertEquals("Saturn", list2.get(5));
        assertEquals("Uranus", list2.get(6));
        assertEquals("Neptune", list2.get(7));
        assertEquals("Pluto", list2.get(8));
        //
        assertEquals(list1, list2);
        assertNotSame(list1, list2);
    }

    public void testArray() {
        Integer[] array = new Integer[3];
        array[0] = new Integer(1);
        array[1] = new Integer(1);
        array[2] = new Integer(2);
        String output = dump(array);
        assertEquals("[1, 1, 2]\n", output);
    }

    public void testArrayPrimitives() {
        int[] array = new int[3];
        array[0] = 1;
        array[1] = 1;
        array[2] = 2;
        try {
            dump(array);
            fail("Arrays of primitives are not supported.");
        } catch (RuntimeException e) {
            assertEquals("Arrays of primitives are not fully supported.", e.getMessage());
        }
    }
}
