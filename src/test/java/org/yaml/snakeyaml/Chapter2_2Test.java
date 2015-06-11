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
package org.yaml.snakeyaml;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Test Chapter 2.2 from the YAML specification
 * 
 * @see http://yaml.org/spec/1.1/
 */
public class Chapter2_2Test extends TestCase {

    @SuppressWarnings("unchecked")
    public void testExample_2_7() {
        YamlStream resource = new YamlStream("example2_7.yaml");
        List<Object> list = (List<Object>) resource.getNativeData();
        assertEquals(2, list.size());
        List<String> list1 = (List<String>) list.get(0);
        assertEquals(3, list1.size());
        assertEquals("Mark McGwire", list1.get(0));
        assertEquals("Sammy Sosa", list1.get(1));
        assertEquals("Ken Griffey", list1.get(2));
        List<String> list2 = (List<String>) list.get(1);
        assertEquals(2, list2.size());
        assertEquals("Chicago Cubs", list2.get(0));
        assertEquals("St Louis Cardinals", list2.get(1));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_8() {
        YamlStream resource = new YamlStream("example2_8.yaml");
        List<Object> list = (List<Object>) resource.getNativeData();
        assertEquals(2, list.size());
        Map<String, String> map1 = (Map<String, String>) list.get(0);
        assertEquals(3, map1.size());
        assertEquals(new Integer(72200), map1.get("time"));
        assertEquals("Sammy Sosa", map1.get("player"));
        assertEquals("strike (miss)", map1.get("action"));
        Map<String, String> map2 = (Map<String, String>) list.get(1);
        assertEquals(3, map2.size());
        assertEquals(new Integer(72227), map2.get("time"));
        assertEquals("Sammy Sosa", map2.get("player"));
        assertEquals("grand slam", map2.get("action"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_9() {
        YamlDocument document = new YamlDocument("example2_9.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(map.toString(), 2, map.size());
        List<String> list1 = (List<String>) map.get("hr");
        assertEquals(2, list1.size());
        assertEquals("Mark McGwire", list1.get(0));
        assertEquals("Sammy Sosa", list1.get(1));
        List<String> list2 = (List<String>) map.get("rbi");
        assertEquals(2, list2.size());
        assertEquals("Sammy Sosa", list2.get(0));
        assertEquals("Ken Griffey", list2.get(1));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_10() {
        YamlDocument document = new YamlDocument("example2_10.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals("Examples 2.9 and 2.10 must be identical.",
                new YamlDocument("example2_9.yaml").getNativeData(), map);
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_11() {
        YamlDocument document = new YamlDocument("example2_11.yaml");
        Map<Object, Object> map = (Map<Object, Object>) document.getNativeData();
        assertEquals(2, map.size());
        for (Object key : map.keySet()) {
            List<String> list = (List<String>) key;
            assertEquals(2, list.size());
        }
    }

    public void testExample_2_12() {
        YamlDocument document = new YamlDocument("example2_12.yaml");
        @SuppressWarnings("unchecked")
        List<Map<Object, Object>> list = (List<Map<Object, Object>>) document.getNativeData();
        assertEquals(3, list.size());
        Map<Object, Object> map1 = (Map<Object, Object>) list.get(0);
        assertEquals(2, map1.size());
        assertEquals("Super Hoop", map1.get("item"));
        Map<Object, Object> map2 = (Map<Object, Object>) list.get(1);
        assertEquals(2, map2.size());
        assertEquals("Basketball", map2.get("item"));
        Map<Object, Object> map3 = (Map<Object, Object>) list.get(2);
        assertEquals(2, map3.size());
        assertEquals("Big Shoes", map3.get("item"));
    }
}
