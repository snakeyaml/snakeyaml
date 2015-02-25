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
 * @see http://yaml.org/type/omap.html
 */
public class OmapTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testOmap() {
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
