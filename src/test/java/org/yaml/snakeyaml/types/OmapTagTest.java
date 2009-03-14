/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.types;

import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/omap.html
 */
public class OmapTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testOmap() throws IOException {
        YamlDocument document = new YamlDocument("types/omap.yaml");
        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) document
                .getNativeData();
        assertEquals(2, map.size());
        Map<String, String> map1 = (Map<String, String>) map.get("Bestiary");
        assertEquals(3, map1.size());
        assertEquals("African pig-like ant eater. Ugly.", map1.get("aardvark"));
        assertEquals("South-American ant eater. Two species.", map1.get("anteater"));
        assertEquals("South-American constrictor snake. Scaly.", map1.get("anaconda"));
        //
        Map<String, String> map2 = (Map<String, String>) map.get("Numbers");
        assertEquals(3, map2.size());
        assertEquals(new Integer(1), map2.get("one"));
        assertEquals(new Integer(2), map2.get("two"));
        assertEquals(new Integer(3), map2.get("three"));
    }

}
