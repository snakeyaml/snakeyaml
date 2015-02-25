/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.types;

import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/map.html
 */
public class MapTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testMap() {
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
    public void testMapYaml11() {
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
