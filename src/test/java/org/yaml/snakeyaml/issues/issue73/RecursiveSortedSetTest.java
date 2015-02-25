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
package org.yaml.snakeyaml.issues.issue73;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class RecursiveSortedSetTest extends TestCase {
    public void testDumpException() {
        SortedSet<Object> set = new TreeSet<Object>();
        Bean11 bean = new Bean11();
        bean.setId("ID555");
        bean.setSet(set);
        set.add("ggg");
        set.add("hhh");
        set.add(bean);
        Yaml yaml = new Yaml();
        String doc = yaml.dump(bean);
        // System.out.println(doc);
        assertEquals(Util.getLocalResource("issues/issue73-recursive9.txt"), doc);
    }

    public void testLoadException() {
        String doc = Util.getLocalResource("issues/issue73-recursive10.txt");
        // System.out.println(doc);
        Yaml yaml = new Yaml();
        try {
            yaml.load(doc);
            fail("Recursive sets are not supported.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Set cannot be recursive."));
        }
    }

    /**
     * set and JavaBean refer to each other
     */
    public void testLoadRecursiveTest() {
        String doc = Util.getLocalResource("issues/issue73-recursive9.txt");
        // System.out.println(doc);
        Yaml yaml = new Yaml();
        Bean11 beanWithSet = (Bean11) yaml.load(doc);
        Set<Object> set = beanWithSet.getSet();
        assertEquals(TreeSet.class, set.getClass());
        assertEquals("ID555", beanWithSet.getId());
        assertEquals(3, set.size());
        assertTrue(set.remove("ggg"));
        // assertFalse(set.remove("ggg"));???
        assertTrue(set.remove("hhh"));
        assertEquals(1, set.size());
        //
        Bean11 beanRef = (Bean11) set.iterator().next();
        assertEquals(beanWithSet, beanRef);
        assertSame(beanWithSet, beanRef);
        //
        assertFalse(set.isEmpty());
        assertTrue(set.contains(beanWithSet));
        assertFalse(set.add(beanWithSet));
        assertTrue(set.remove(beanWithSet));
        assertFalse(set.remove(beanWithSet));
        assertTrue(set.isEmpty());
    }

    public static class Bean11 implements Comparable<Object> {
        private SortedSet<Object> set;
        private String id;

        public SortedSet<Object> getSet() {
            return set;
        }

        public void setSet(SortedSet<Object> set) {
            this.set = set;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int compareTo(Object o) {
            return toString().compareTo(o.toString());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Bean11) {
                Bean11 b = (Bean11) obj;
                return id.equals(b.id);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public String toString() {
            return "Bean id=" + id + "set=" + System.identityHashCode(set);
        }
    }
}
