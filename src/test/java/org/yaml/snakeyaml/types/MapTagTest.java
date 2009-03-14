/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/map.html
 */
public class MapTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testMap() throws IOException {
        YamlDocument document = new YamlDocument("types/map.yaml");
        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) document
                .getNativeData();
        assertEquals(2, map.size());
        Map<String, String> map1 = (Map<String, String>) map.get("Block style");
        assertEquals(3, map1.size());
        assertEquals("Evans", map1.get("Clark"));
        assertEquals("Ingerson", map1.get("Brian"));
        assertEquals("Ben-Kiki", map1.get("Oren"));
        //
        Map<String, String> map2 = (Map<String, String>) map.get("Flow style");
        assertEquals(3, map2.size());
        assertEquals("Evans", map2.get("Clark"));
        assertEquals("Ingerson", map2.get("Brian"));
        assertEquals("Ben-Kiki", map2.get("Oren"));
        //
        assertEquals(map1, map2);
        assertNotSame(map1, map2);
    }

    @SuppressWarnings("unchecked")
    public void testMapYaml11() throws IOException {
        YamlDocument document = new YamlDocument("types/map_mixed_tags.yaml");
        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) document
                .getNativeData();
        assertEquals(2, map.size());
        Map<String, String> map1 = (Map<String, String>) map.get("Block style");
        assertEquals(3, map1.size());
        assertEquals("Evans", map1.get("Clark"));
        assertEquals("Ingerson", map1.get("Brian"));
        assertEquals("Ben-Kiki", map1.get("Oren"));
        //
        Map<String, String> map2 = (Map<String, String>) map.get("Flow style");
        assertEquals(3, map2.size());
        assertEquals("Evans", map2.get("Clark"));
        assertEquals("Ingerson", map2.get("Brian"));
        assertEquals("Ben-Kiki", map2.get("Oren"));
        //
        assertEquals(map1, map2);
    }

}
