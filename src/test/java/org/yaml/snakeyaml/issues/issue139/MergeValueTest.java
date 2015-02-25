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
package org.yaml.snakeyaml.issues.issue139;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class MergeValueTest extends TestCase {

    public void testNotUniqueSimple() {
        String simple = "{key: 1, key: 2}";
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, Integer> map = (Map<String, Integer>) yaml.load(simple);
        assertEquals(1, map.size());
        assertEquals(new Integer(2), map.get("key"));
    }

    public void testMerge() {
        check("issues/issue139-1.yaml");// merge with unique keys
        check("issues/issue139-2.yaml");// merge with same key
    }

    private void check(String name) {
        String input = Util.getLocalResource(name);
        // System.out.println(input);
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) yaml.load(input);
        assertEquals(2, map.size());
        assertTrue(map.containsKey("common"));
        assertTrue(map.containsKey("production"));
        assertEquals(map.get("common"), map.get("production"));
    }

    /**
     * http://yaml.org/type/merge.html: If the value associated with the key is
     * a single mapping node, each of its key/value pairs is inserted into the
     * current mapping, <b>unless the key already exists in it</b>.
     */
    @SuppressWarnings("unchecked")
    public void testMergeUnlessAlreadyExists() {
        String input = Util.getLocalResource("issues/issue139-3.yaml");
        // System.out.println(input);
        Yaml yaml = new Yaml();
        Map<String, Object> map = (Map<String, Object>) yaml.load(input);
        assertEquals(2, map.size());
        Map<String, Integer> common = (Map<String, Integer>) map.get("common");
        Map<String, Integer> production = (Map<String, Integer>) map.get("production");
        assertEquals(new Integer(2), common.get("key"));
        assertEquals(new Integer(3), production.get("key"));
    }
}
