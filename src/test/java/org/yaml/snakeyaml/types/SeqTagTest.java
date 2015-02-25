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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/seq.html
 */
public class SeqTagTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    public void testSeq() {
        YamlDocument document = new YamlDocument("types/seq.yaml");
        Map<String, List<String>> map = (Map<String, List<String>>) document.getNativeData();
        assertEquals(2, map.size());
        List<String> list1 = (List<String>) map.get("Block style");
        assertEquals(9, list1.size());
        assertEquals("Mercury", list1.get(0));
        assertEquals("Venus", list1.get(1));
        assertEquals("Earth", list1.get(2));
        assertEquals("Mars", list1.get(3));
        assertEquals("Jupiter", list1.get(4));
        assertEquals("Saturn", list1.get(5));
        assertEquals("Uranus", list1.get(6));
        assertEquals("Neptune", list1.get(7));
        assertEquals("Pluto", list1.get(8));
        //
        List<String> list2 = (List<String>) map.get("Flow style");
        assertEquals(9, list2.size());
        assertEquals("Mercury", list2.get(0));
        assertEquals("Venus", list2.get(1));
        assertEquals("Earth", list2.get(2));
        assertEquals("Mars", list2.get(3));
        assertEquals("Jupiter", list2.get(4));
        assertEquals("Saturn", list2.get(5));
        assertEquals("Uranus", list2.get(6));
        assertEquals("Neptune", list2.get(7));
        assertEquals("Pluto", list2.get(8));
        //
        assertEquals(list1, list2);
        assertNotSame(list1, list2);
    }

    @SuppressWarnings("unchecked")
    public void testCollection() {
        Collection<Integer> collection = new LinkedList<Integer>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        String output = dump(collection);
        assertEquals("[1, 2, 3]\n", output);
        Collection<Integer> obj = (Collection<Integer>) load(output);
        // System.out.println(obj);
        assertEquals(3, obj.size());
        assertTrue("Create ArrayList by default: " + obj.getClass().toString(),
                obj instanceof ArrayList);
    }

    public void testArray() {
        Integer[] array = new Integer[3];
        array[0] = new Integer(1);
        array[1] = new Integer(1);
        array[2] = new Integer(2);
        String output = dump(array);
        assertEquals("[1, 1, 2]\n", output);
    }

    public void testStringArray() {
        String[] array = new String[] { "aaa", "bbb", "ccc" };
        String output = dump(array);
        assertEquals("[aaa, bbb, ccc]\n", output);
    }
}
