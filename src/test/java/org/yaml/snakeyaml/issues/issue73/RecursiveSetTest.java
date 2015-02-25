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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class RecursiveSetTest extends TestCase {
    public void testDumpException() {
        Set<Object> set1 = new HashSet<Object>();
        Set<Object> set2 = new HashSet<Object>();
        set1.add(set2);
        set2.add(set1);
        Yaml yaml = new Yaml();
        try {
            yaml.dump(set1);
            fail("Recursive sets are not supported.");
        } catch (StackOverflowError e) {
            assertEquals(null, e.getMessage());
        }
    }

    public void testLoadException() {
        String doc = Util.getLocalResource("issues/issue73-recursive4.txt");
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
     * XXX: sets can be recursive
     */
    @SuppressWarnings("unchecked")
    public void testLoadRecursiveTest() {
        String doc = Util.getLocalResource("issues/issue73-recursive5.txt");
        // System.out.println(doc);
        Yaml yaml = new Yaml();
        Bean1 obj = (Bean1) yaml.load(doc);
        Set<Object> set = obj.getSet();
        // System.out.println(set);
        assertEquals(LinkedHashSet.class, set.getClass());
        assertEquals("ID123", obj.getId());
        assertEquals(3, set.size());
        assertTrue(set.remove("zzz"));
        assertTrue(set.remove("ccc"));
        assertFalse(set.contains("111"));
        try {
            set.contains(set);
            fail("Recursive set fails to provide a hashcode.");
        } catch (StackOverflowError e) {
            // ignore
        }
        //
        Set<Object> self = (Set<Object>) set.iterator().next();
        assertEquals(LinkedHashSet.class, self.getClass());
        assertEquals(set, self);
        assertSame(set, self);
        assertEquals(1, set.size());
        assertEquals(1, self.size());
        set.add("111");
        assertEquals(2, set.size());
        assertEquals(2, self.size());
        //
        self.clear();
        assertTrue(self.isEmpty());
        assertTrue(set.isEmpty());
        assertFalse("Now it should not be recursive any longer (no StackOverflowError).",
                set.contains(set));
        //
        set.add("jjj");
        assertEquals(1, set.size());
        assertEquals(1, self.size());
    }

    public static class Bean1 {
        private Set<Object> set;
        private String id;

        public Set<Object> getSet() {
            return set;
        }

        public void setSet(Set<Object> set) {
            this.set = set;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
