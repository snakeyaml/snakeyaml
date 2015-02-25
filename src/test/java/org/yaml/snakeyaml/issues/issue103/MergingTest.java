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
package org.yaml.snakeyaml.issues.issue103;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class MergingTest extends TestCase {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testMergeWithDefaultMap() {
        String input = Util.getLocalResource("issues/issue103.yaml");
        // System.out.println(input);
        Yaml yaml = new Yaml();

        check((Map) yaml.load(input));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testMergeWithFakeMap() {
        String input = Util.getLocalResource("issues/issue103.yaml");
        // System.out.println(input);
        Constructor c = new Constructor() {
            protected java.util.Map<Object, Object> createDefaultMap() {
                return new FakeMap<Object, Object>();
            }
        };

        Yaml yaml = new Yaml(c);

        check((FakeMap) yaml.load(input));
    }

    private void check(Map<String, List<Map<Object, Object>>> map) {

        assertEquals(2, map.size());
        assertTrue(map.containsKey("input"));
        assertTrue(map.containsKey("result"));

        // input: ...
        List<Map<Object, Object>> inputList = map.get("input");
        assertEquals(4, inputList.size());

        Map<Object, Object> center = inputList.get(0);
        assertEquals(2, center.size());
        assertEquals(Integer.valueOf(1), center.get("x"));
        assertEquals(Integer.valueOf(2), center.get("y"));

        Map<Object, Object> left = inputList.get(1);
        assertEquals(2, left.size());
        assertEquals(Integer.valueOf(0), left.get("x"));
        assertEquals(Integer.valueOf(2), left.get("y"));

        Map<Object, Object> big = inputList.get(2);
        assertEquals(1, big.size());
        assertEquals(Integer.valueOf(10), big.get("r"));

        Map<Object, Object> small = inputList.get(3);
        assertEquals(1, small.size());
        assertEquals(Integer.valueOf(1), small.get("r"));

        // result : ...
        List<Map<Object, Object>> resultList = map.get("result");
        assertEquals(5, resultList.size());

        Map<Object, Object> explicitKeys = resultList.get(0);
        assertEquals(4, explicitKeys.size());
        assertEquals(Integer.valueOf(1), explicitKeys.get("x"));
        assertEquals(Integer.valueOf(2), explicitKeys.get("y"));
        assertEquals(Integer.valueOf(10), explicitKeys.get("r"));
        assertEquals("center/big", explicitKeys.get("label"));

        Map<?, ?> merge_center = resultList.get(1);
        assertEquals(4, merge_center.size());
        assertEquals(Integer.valueOf(1), merge_center.get("x"));
        assertEquals(Integer.valueOf(2), merge_center.get("y"));
        assertEquals(Integer.valueOf(10), merge_center.get("r"));
        assertEquals("center/big", merge_center.get("label"));

        Map<?, ?> merge_left_override = resultList.get(2);
        assertEquals(4, merge_left_override.size());
        assertEquals(Integer.valueOf(0), merge_left_override.get("x"));
        assertEquals(Integer.valueOf(5), merge_left_override.get("y"));
        assertEquals(Integer.valueOf(10), merge_left_override.get("r"));
        assertEquals("center/big", merge_left_override.get("label"));

        Map<?, ?> merge_center_big = resultList.get(3);
        assertEquals(4, merge_center_big.size());
        assertEquals(Integer.valueOf(1), merge_center_big.get("x"));
        assertEquals(Integer.valueOf(2), merge_center_big.get("y"));
        assertEquals(Integer.valueOf(10), merge_center_big.get("r"));
        assertEquals("center/big", merge_center_big.get("label"));

        Map<?, ?> merge_big_left_small_override = resultList.get(4);
        assertEquals(4, merge_big_left_small_override.size());
        assertEquals(Integer.valueOf(1), merge_big_left_small_override.get("x"));
        assertEquals(Integer.valueOf(2), merge_big_left_small_override.get("y"));
        assertEquals(Integer.valueOf(10), merge_big_left_small_override.get("r"));
        assertEquals("center/big", merge_big_left_small_override.get("label"));
    }
}
