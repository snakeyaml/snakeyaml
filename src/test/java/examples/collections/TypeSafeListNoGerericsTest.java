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
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

/**
 * Test ListBean->List developers <br/>
 * Developer class cannot be properly recognised
 */
public class TypeSafeListNoGerericsTest extends TestCase {
    public void testDumpList() {
        ListBean bean = new ListBean();
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        bean.setChildren(list);
        List<Developer> developers = new ArrayList<Developer>();
        developers.add(new Developer("Fred", "creator"));
        developers.add(new Developer("John", "committer"));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/list-bean-4.yaml");
        assertEquals(etalon, output);
    }

    @SuppressWarnings("unchecked")
    public void testLoadList() {
        String output = Util.getLocalResource("examples/list-bean-1.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        ListBean parsed = beanLoader.loadAs(output, ListBean.class);
        assertNotNull(parsed);
        List<String> list2 = parsed.getChildren();
        assertEquals(2, list2.size());
        assertEquals("aaa", list2.get(0));
        assertEquals("bbb", list2.get(1));
        List<Map<String, String>> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        Map<String, String> fred = developers.get(0);
        assertEquals("Fred", fred.get("name"));
        assertEquals("creator", fred.get("role"));
    }

    @SuppressWarnings("rawtypes")
    public static class ListBean {
        private List<String> children;
        private String name;
        private List developers;

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

        public List getDevelopers() {
            return developers;
        }

        public void setDevelopers(List developers) {
            this.developers = developers;
        }
    }

    public static class Developer {
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
}
