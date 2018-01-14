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
package org.yaml.snakeyaml.json;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;


import java.util.Map;

public class JsonTest extends TestCase {

    private Yaml loader = new Yaml();


    public void testLooksLikeJson() {
        Map<String, Integer> map = (Map<String, Integer>) loader.load("{a: 1}");
        assertEquals(new Integer(1), map.get("a"));
    }

    public void testSpaceAfterColon() {
        Map<String, Integer> map = (Map<String, Integer>) loader.load("{\"a\": 1}");
        assertEquals(new Integer(1), map.get("a"));
    }

    public void testCounterintuitiveColon() {
        try {
            loader.load("{a:1}");
            fail("We agree with libyaml and PyYAML.");
        } catch (Exception e) {
            assertTrue("':' in the flow context is a mess.", e.getMessage().contains("Please check http://pyyaml.org/wiki/YAMLColonInFlowContext for details."));
        }
    }

    public void testNoSpace() {
        Map<String, Integer> map = (Map<String, Integer>) loader.load("{\"a\":1}");
        assertEquals(new Integer(1), map.get("a"));
    }

    public void testNoSpaceBothDoubleQuoted() {
        Map<String, Integer> map = (Map<String, Integer>) loader.load("{\"a\":\"1\"}");
        assertEquals("1", map.get("a"));
    }

    public void testNoSpaceSingleQouted() {
        Map<String, Integer> map = (Map<String, Integer>) loader.load("{'a':1}");
        assertEquals(new Integer(1), map.get("a"));
    }

    public void testManyValues() {
        Map<String, Object> map = (Map<String, Object>) loader.load("{\"a\":1,\"b\":true,\"c\":\"foo\"}");
        assertEquals(3, map.size());
        assertEquals(new Integer(1), map.get("a"));
        assertTrue((Boolean) map.get("b"));
        assertEquals("foo", map.get("c"));
    }

    public void testConstructNull() {
        Map<String, Object> map = (Map<String, Object>) loader.load("{a: null}");
        assertEquals(1, map.size());
        assertNull(map.get("a"));
    }

    public void testConstructNullFromEmpty() {
        Map<String, Object> map = (Map<String, Object>) loader.load("{a: }");
        assertEquals(1, map.size());
        assertNull(map.get("a"));
    }

    public void testConstructBoolean() {
        Map<String, Object> map = (Map<String, Object>) loader.load("{a: true}");
        assertEquals(1, map.size());
        assertEquals(Boolean.TRUE, map.get("a"));
    }
}
