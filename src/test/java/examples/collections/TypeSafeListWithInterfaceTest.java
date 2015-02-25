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
package examples.collections;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

/**
 * Test ListBean->List<Human> developers <br/>
 * Human is an interface and the global tags are required
 */
public class TypeSafeListWithInterfaceTest extends TestCase {
    public void testDumpList() {
        ListBean bean = new ListBean();
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        bean.setChildren(list);
        List<Human> developers = new ArrayList<Human>();
        developers.add(new Developer("Fred", "creator"));
        developers.add(new Committer("John", "committer", 34));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/list-bean-2.yaml");
        assertEquals(etalon, output);
    }

    public void testLoadWrongList() {
        String output = Util.getLocalResource("examples/list-bean-1.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        try {
            beanLoader.loadAs(output, ListBean.class);
            fail("Global tags are required since Human is an interface.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Cannot create property=developers"));
        }
    }

    public void testLoadList() {
        String output = Util.getLocalResource("examples/list-bean-2.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        ListBean parsed = beanLoader.loadAs(output, ListBean.class);
        assertNotNull(parsed);
        List<String> list2 = parsed.getChildren();
        assertEquals(2, list2.size());
        assertEquals("aaa", list2.get(0));
        assertEquals("bbb", list2.get(1));
        List<Human> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        assertEquals("Developer must be recognised.", Developer.class, developers.get(0).getClass());
        Developer fred = (Developer) developers.get(0);
        assertEquals("Fred", fred.getName());
        assertEquals("creator", fred.getRole());
        Committer john = (Committer) developers.get(1);
        assertEquals("John", john.getName());
        assertEquals("committer", john.getRole());
        assertEquals(34, john.getKey());
    }

    public static class ListBean {
        private List<String> children;
        private String name;
        private List<Human> developers;

        public ListBean() {
            name = "Bean123";
        }

        public List<String> getChildren() {
            return children;
        }

        public void setChildren(List<String> children) {
            this.children = children;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Human> getDevelopers() {
            return developers;
        }

        public void setDevelopers(List<Human> developers) {
            this.developers = developers;
        }
    }

    public static interface Human {

        public String getName();

        public void setName(String name);

    }

    public static class Developer implements Human {
        private String name;
        private String role;

        public Developer() {
        }

        public Developer(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    public static class Committer extends Developer {
        private int key;

        public Committer() {
        }

        public Committer(String string, String string2, int i) {
            super(string, string2);
            this.key = i;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }
    }
}
