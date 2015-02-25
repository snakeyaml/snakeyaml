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

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

/**
 * Test MapBean->Map<String, Developer> developers <br/>
 * Developer class must be properly recognised
 */
public class TypeSafeMapTest extends TestCase {
    public void testDumpMap() {
        MapBean bean = new MapBean();
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        data.put("aaa", 1);
        data.put("bbb", 2);
        data.put("zzz", 3);
        bean.setData(data);
        Map<String, Developer2> developers = new LinkedHashMap<String, Developer2>();
        developers.put("team1", new Developer2("Fred", "creator"));
        developers.put("team2", new Developer2("John", "committer"));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-10.yaml");
        assertEquals(etalon, output);
    }

    public void testDumpMap2() {
        MapBean bean = new MapBean();
        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        data.put("aaa", 1);
        data.put("bbb", 2);
        bean.setData(data);
        Map<String, Developer2> developers = new LinkedHashMap<String, Developer2>();
        developers.put("team1", new Developer2("Fred", "creator"));
        developers.put("team2", new Developer2("John", "committer"));
        developers.put("team3", new Developer222("Bill", "head"));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-11.yaml");
        assertEquals(etalon, output);
    }

    public void testLoadMap() {
        String output = Util.getLocalResource("examples/map-bean-10.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        MapBean parsed = beanLoader.loadAs(output, MapBean.class);
        assertNotNull(parsed);
        Map<String, Integer> data = parsed.getData();
        assertEquals(3, data.size());
        assertEquals(new Integer(1), data.get("aaa"));
        assertEquals(new Integer(2), data.get("bbb"));
        assertEquals(new Integer(3), data.get("zzz"));
        Map<String, Developer2> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        assertEquals("Developer must be recognised.", Developer2.class, developers.get("team1")
                .getClass());
        Developer2 fred = developers.get("team1");
        assertEquals("Fred", fred.getName());
        assertEquals("creator", fred.getRole());
    }

    public static class MapBean {
        private Map<String, Integer> data;
        private String name;
        private Map<String, Developer2> developers;

        public MapBean() {
            name = "Bean123";
        }

        public Map<String, Integer> getData() {
            return data;
        }

        public void setData(Map<String, Integer> data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, Developer2> getDevelopers() {
            return developers;
        }

        public void setDevelopers(Map<String, Developer2> developers) {
            this.developers = developers;
        }
    }

    public static class Developer2 {
        private String name;
        private String role;

        public Developer2() {
        }

        public Developer2(String name, String role) {
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

    public static class Developer222 extends Developer2 {
        public Developer222() {
            super();
        }

        public Developer222(String name, String role) {
            super(name, role);
        }
    }

    /*
     * No generic collection
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testLoadMapWithObject() {
        String output = Util.getLocalResource("examples/map-bean-10.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        MapBeanNoGenerics parsed = beanLoader.loadAs(output, MapBeanNoGenerics.class);
        assertNotNull(parsed);
        Map<String, Integer> data = parsed.getData();
        assertEquals(3, data.size());
        assertEquals(new Integer(1), data.get("aaa"));
        assertEquals(new Integer(2), data.get("bbb"));
        assertEquals(new Integer(3), data.get("zzz"));
        Map developers = parsed.getDevelopers();
        assertNotNull(developers);
        assertEquals(2, developers.size());
        Object o1 = developers.get("team1");
        // because of erasure we get simply Map
        Map<String, String> developer = (Map<String, String>) o1;
        assertEquals("Fred", developer.get("name"));
        assertEquals("creator", developer.get("role"));
    }

    @SuppressWarnings("rawtypes")
    public static class MapBeanNoGenerics {
        private Map data;
        private String name;
        private Map developers;

        public MapBeanNoGenerics() {
            name = "Bean123";
        }

        public Map getData() {
            return data;
        }

        public void setData(Map data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map getDevelopers() {
            return developers;
        }

        public void setDevelopers(Map developers) {
            this.developers = developers;
        }
    }
}
