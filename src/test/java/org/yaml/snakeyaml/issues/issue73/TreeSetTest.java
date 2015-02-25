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

import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * Test bean when the implementation is defined: TreeSet instead of just the
 * interface Set
 */
public class TreeSetTest extends TestCase {
    public void testSetImplementation() {
        Bean1 bean = new Bean1();
        bean.setId("ID123");
        TreeSet<String> list = new TreeSet<String>();
        list.add("zzz");
        list.add("xxx");
        list.add("ccc");
        bean.setSet(list);
        Yaml yaml = new Yaml();
        String doc = yaml.dump(bean);
        // System.out.println(doc);
        //
        Bean1 loaded = (Bean1) yaml.load(doc);
        assertEquals(3, loaded.getSet().size());
        assertEquals(TreeSet.class, loaded.getSet().getClass());
        assertTrue(loaded.getSet().contains("zzz"));
        assertTrue(loaded.getSet().contains("xxx"));
        assertTrue(loaded.getSet().contains("ccc"));
    }

    public static class Bean1 {
        private TreeSet<String> set;
        private String id;

        public TreeSet<String> getSet() {
            return set;
        }

        public void setSet(TreeSet<String> set) {
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
