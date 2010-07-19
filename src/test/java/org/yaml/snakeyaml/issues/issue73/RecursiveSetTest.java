/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class RecursiveSetTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testDumpException() {
        Set set1 = new HashSet();
        Set set2 = new HashSet();
        set1.add(set2);
        set2.add(set1);
        Yaml yaml = new Yaml();
        try {
            yaml.dump(set1);
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
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Set cannot be recursive."));
        }
    }

    public void testLoadException2() {
        String doc = Util.getLocalResource("issues/issue73-recursive5.txt");
        // System.out.println(doc);
        Yaml yaml = new Yaml();
        try {
            yaml.load(doc);
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Set cannot be recursive."));
        }
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
