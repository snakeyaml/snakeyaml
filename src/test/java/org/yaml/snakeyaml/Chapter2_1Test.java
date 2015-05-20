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
 * Test Chapter 2.1 from the YAML specification
 * 
 * @see http://yaml.org/spec/1.1/
 */
public class Chapter2_1Test extends TestCase {

    @SuppressWarnings("unchecked")
    public void testExample_2_1() {
        YamlDocument document = new YamlDocument("example2_1.yaml");
        List<String> list = (List<String>) document.getNativeData();
        assertEquals(3, list.size());
        assertEquals("Mark McGwire", list.get(0));
        assertEquals("Sammy Sosa", list.get(1));
        assertEquals("Ken Griffey", list.get(2));
        assertEquals("[Mark McGwire, Sammy Sosa, Ken Griffey]\n", document.getPresentation());
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_2() {
        YamlDocument document = new YamlDocument("example2_2.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(3, map.size());
        assertEquals("Expect 65 to be a Integer", Integer.class, map.get("hr").getClass());
        assertEquals(new Integer(65), map.get("hr"));
        assertEquals(new Float(0.278), new Float("0.278"));
        assertEquals("Expect 0.278 to be a Float", Double.class, map.get("avg").getClass());
        assertEquals(new Double(0.278), map.get("avg"));
        assertEquals("Expect 147 to be an Integer", Integer.class, map.get("rbi").getClass());
        assertEquals(new Integer(147), map.get("rbi"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_3() {
        YamlDocument document = new YamlDocument("example2_3.yaml");
        Map<String, List<String>> map = (Map<String, List<String>>) document.getNativeData();
        assertEquals(2, map.size());
        List<String> list1 = map.get("american");
        assertEquals(3, list1.size());
        assertEquals("Boston Red Sox", list1.get(0));
        assertEquals("Detroit Tigers", list1.get(1));
        assertEquals("New York Yankees", list1.get(2));
        List<String> list2 = map.get("national");
        assertEquals(3, list2.size());
        assertEquals("New York Mets", list2.get(0));
        assertEquals("Chicago Cubs", list2.get(1));
        assertEquals("Atlanta Braves", list2.get(2));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_4() {
        YamlDocument document = new YamlDocument("example2_4.yaml");
        List<Map<String, Object>> list = (List<Map<String, Object>>) document.getNativeData();
        assertEquals(2, list.size());
        Map<String, Object> map1 = list.get(0);
        assertEquals(3, map1.size());
        assertEquals("Mark McGwire", map1.get("name"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_5() {
        YamlDocument document = new YamlDocument("example2_5.yaml");
        List<List<Object>> list = (List<List<Object>>) document.getNativeData();
        assertEquals(3, list.size());
        List<Object> list1 = list.get(0);
        assertEquals(3, list1.size());
        assertEquals("name", list1.get(0));
        assertEquals("hr", list1.get(1));
        assertEquals("avg", list1.get(2));
        assertEquals(3, list.get(1).size());
        assertEquals(3, list.get(2).size());
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_6() {
        YamlDocument document = new YamlDocument("example2_6.yaml");
        Map<String, Map<String, Object>> map = (Map<String, Map<String, Object>>) document
                .getNativeData();
        assertEquals(2, map.size());
        Map<String, Object> map1 = map.get("Mark McGwire");
        assertEquals(2, map1.size());
        Map<String, Object> map2 = map.get("Sammy Sosa");
        assertEquals(2, map2.size());
    }
}
