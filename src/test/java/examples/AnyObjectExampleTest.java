/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class AnyObjectExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testLoad() throws IOException {
        String doc = Util.getLocalResource("examples/any-object-example.yaml");
        System.out.println(doc);
        Yaml yaml = new Yaml();
        Map<String, Object> object = (Map<String, Object>) yaml.load(doc);
        System.out.println(object);
        assertEquals(6, object.size());
        assertEquals("[null, null]", object.get("none").toString());
        List list1 = (List) object.get("none");
        assertEquals(2, list1.size());
        for (Object object2 : list1) {
            assertNull(object2);
        }
        //
        assertEquals("[true, false, true, false]", object.get("bool").toString());
        assertEquals(4, ((List) object.get("bool")).size());
        //
        assertEquals(new Integer(42), object.get("int"));
        assertEquals(new Double(3.14159), object.get("float"));
        //
        assertEquals("[LITE, RES_ACID, SUS_DEXT]", object.get("list").toString());
        List list2 = (List) object.get("list");
        assertEquals(3, list2.size());
        for (Object object2 : list2) {
            assertEquals(object2.toString(), object2.toString().toUpperCase());
        }
        //
        assertEquals("{hp=13, sp=5}", object.get("dict").toString());
        Map<String, Integer> map = (Map<String, Integer>) object.get("dict");
        assertEquals(2, map.keySet().size());
        assertEquals(new Integer(13), map.get("hp"));
        assertEquals(new Integer(5), map.get("sp"));
    }
}
