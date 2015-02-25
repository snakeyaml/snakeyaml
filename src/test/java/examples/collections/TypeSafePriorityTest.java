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

import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Test ListBean->List<Human> developers <br/>
 * Human is an interface and the global tags are required
 */
public class TypeSafePriorityTest extends TestCase {

    /**
     * explicit TypeDescription is more important then runtime class (which may
     * be an interface)
     */
    public void testLoadList2() {
        String output = Util.getLocalResource("examples/list-bean-3.yaml");
        // System.out.println(output);
        TypeDescription descr = new TypeDescription(ListBean.class);
        descr.putListPropertyType("developers", Developer.class);
        Yaml beanLoader = new Yaml(new Constructor(descr));
        ListBean parsed = beanLoader.loadAs(output, ListBean.class);
        assertNotNull(parsed);
        List<Human> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        assertEquals("Committer must be recognised.", Developer.class, developers.get(0).getClass());
        Developer fred = (Developer) developers.get(0);
        assertEquals("Fred", fred.getName());
        assertEquals("creator", fred.getRole());
        Developer john = (Developer) developers.get(1);
        assertEquals("John", john.getName());
        assertEquals("committer", john.getRole());
    }

    public static class ListBean {
        private String name;
        private List<Human> developers;

        public ListBean() {
            name = "Bean123";
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

}
