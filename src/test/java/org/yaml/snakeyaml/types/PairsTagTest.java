/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/pairs.html
 */
public class PairsTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testPairs() throws IOException {
        YamlDocument document = new YamlDocument("types/pairs.yaml", false);
        Map<String, List<String[]>> map = (Map<String, List<String[]>>) document.getNativeData();
        assertEquals(2, map.size());
        List<String[]> list1 = (List<String[]>) map.get("Block tasks");
        assertEquals(4, list1.size());
        Object[] tuple1 = list1.get(0);
        assertEquals(2, tuple1.length);
        assertEquals("meeting", tuple1[0]);
        assertEquals("with team.", tuple1[1]);
        //

        Object[] tuple2 = list1.get(1);
        assertEquals(2, tuple2.length);
        assertEquals("meeting", tuple2[0]);
        assertEquals("with boss.", tuple2[1]);
        //

        Object[] tuple3 = list1.get(2);
        assertEquals(2, tuple3.length);
        assertEquals("break", tuple3[0]);
        assertEquals("lunch.", tuple3[1]);
        //

        Object[] tuple4 = list1.get(3);
        assertEquals(2, tuple4.length);
        assertEquals("meeting", tuple4[0]);
        assertEquals("with client.", tuple4[1]);
        //
        List<String[]> list2 = (List<String[]>) map.get("Flow tasks");
        assertEquals(2, list2.size());
        Object[] tuple2_1 = list2.get(0);
        assertEquals(2, tuple2_1.length);
        assertEquals("meeting", tuple2_1[0]);
        assertEquals("with team", tuple2_1[1]);
        //
        Object[] tuple2_2 = list2.get(1);
        assertEquals(2, tuple2_2.length);
        assertEquals("meeting", tuple2_2[0]);
        assertEquals("with boss", tuple2_2[1]);
    }
}
