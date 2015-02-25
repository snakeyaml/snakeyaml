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
package org.pyyaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class PyRecursiveTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testDict() {
        Map<AnInstance, AnInstance> value = new HashMap<AnInstance, AnInstance>();
        AnInstance instance = new AnInstance(value, value);
        value.put(instance, instance);
        Yaml yaml = new Yaml();
        String output1 = yaml.dump(value);
        assertTrue(output1.contains("!!org.pyyaml.AnInstance"));
        assertTrue(output1.contains("&id001"));
        assertTrue(output1.contains("&id002"));
        assertTrue(output1.contains("*id001"));
        assertTrue(output1.contains("*id002"));
        assertTrue(output1.contains("foo"));
        assertTrue(output1.contains("bar"));
        Map<AnInstance, AnInstance> value2 = (Map<AnInstance, AnInstance>) yaml.load(output1);
        assertEquals(value.size(), value2.size());
        for (AnInstance tmpInstance : value2.values()) {
            assertSame(tmpInstance.getBar(), tmpInstance.getFoo());
            assertSame(tmpInstance.getBar(), value2);
            assertSame(tmpInstance, value2.get(tmpInstance));
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testDictSafeConstructor() {
        Map value = new TreeMap();
        value.put("abc", "www");
        value.put("qwerty", value);
        Yaml yaml = new Yaml(new SafeConstructor());
        String output1 = yaml.dump(value);
        assertEquals("&id001\nabc: www\nqwerty: *id001\n", output1);
        Map value2 = (Map) yaml.load(output1);
        assertEquals(2, value2.size());
        assertEquals("www", value2.get("abc"));
        assertTrue(value2.get("qwerty") instanceof Map);
        Map value3 = (Map) value2.get("qwerty");
        assertTrue(value3.get("qwerty") instanceof Map);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testList() {
        List value = new ArrayList();
        value.add(value);
        value.add("test");
        value.add(new Integer(1));

        Yaml yaml = new Yaml();
        String output1 = yaml.dump(value);
        assertEquals("&id001\n- *id001\n- test\n- 1\n", output1);
        List value2 = (List) yaml.load(output1);
        assertEquals(3, value2.size());
        assertEquals(value.size(), value2.size());
        assertSame(value2, value2.get(0));
        // we expect self-reference as 1st element of the list
        // let's remove self-reference and check other "simple" members of the
        // list. otherwise assertEquals will lead us to StackOverflow
        value.remove(0);
        value2.remove(0);
        assertEquals(value, value2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testListSafeConstructor() {
        List value = new ArrayList();
        value.add(value);
        value.add("test");
        value.add(new Integer(1));

        Yaml yaml = new Yaml(new SafeConstructor());
        String output1 = yaml.dump(value);
        assertEquals("&id001\n- *id001\n- test\n- 1\n", output1);
        List value2 = (List) yaml.load(output1);
        assertEquals(3, value2.size());
        assertEquals(value.size(), value2.size());
        assertSame(value2, value2.get(0));
        // we expect self-reference as 1st element of the list
        // let's remove self-reference and check other "simple" members of the
        // list. otherwise assertEquals will lead us to StackOverflow
        value.remove(0);
        value2.remove(0);
        assertEquals(value, value2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testSet() {
        Set value = new HashSet();
        value.add(new AnInstance(value, value));
        Yaml yaml = new Yaml();
        String output1 = yaml.dump(value);
        Set<AnInstance> value2 = (Set<AnInstance>) yaml.load(output1);

        assertEquals(value.size(), value2.size());
        for (AnInstance tmpInstance : value2) {
            assertSame(tmpInstance.getBar(), tmpInstance.getFoo());
            assertSame(tmpInstance.getBar(), value2);
        }
    }

    public void testSet2() {
        Set<Object> set = new HashSet<Object>(3);
        set.add("aaa");
        set.add(111);
        set.add(set);
        Yaml yaml = new Yaml();
        try {
            yaml.dump(set);
            fail("Java does not allow a recursive set to be a key for a map.");
        } catch (StackOverflowError e) {
            // ignore
        }
    }
}
