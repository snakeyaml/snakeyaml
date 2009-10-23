/**
 * Copyright (c) 2008-2009 Andrey Somov
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

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.Util;

/**
 * Test MapBean->Map<Enum, Developer> developers <br/>
 * Developer class must be properly recognised
 */
public class TypeSafeMap2Test extends TestCase {
    public void testDumpMap() {
        MapBean2 bean = new MapBean2();
        Map<Developer2, Color> data = new LinkedHashMap<Developer2, Color>();
        data.put(new Developer2("Andy", "tester"), Color.BLACK);
        data.put(new Developer2("Lisa", "owner"), Color.RED);
        bean.setData(data);
        Map<Color, Developer2> developers = new LinkedHashMap<Color, Developer2>();
        developers.put(Color.WHITE, new Developer2("Fred", "creator"));
        developers.put(Color.BLACK, new Developer2("John", "committer"));
        bean.setDevelopers(developers);
        JavaBeanDumper dumper = new JavaBeanDumper(false);
        String output = dumper.dump(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-12.yaml");
        assertEquals(etalon, output);
    }

    // public void testLoadMap() {
    // String output = Util.getLocalResource("examples/map-bean-10.yaml");
    // // System.out.println(output);
    // JavaBeanLoader<MapBean2> beanLoader = new
    // JavaBeanLoader<MapBean2>(MapBean2.class);
    // MapBean2 parsed = beanLoader.load(output);
    // assertNotNull(parsed);
    // Map<String, Integer> data = parsed.getData();
    // assertEquals(3, data.size());
    // assertEquals(new Integer(1), data.get("aaa"));
    // assertEquals(new Integer(2), data.get("bbb"));
    // assertEquals(new Integer(3), data.get("zzz"));
    // Map<String, Developer2> developers = parsed.getDevelopers();
    // assertEquals(2, developers.size());
    // assertEquals("Developer must be recognised.", Developer2.class,
    // developers.get("team1")
    // .getClass());
    // Developer2 fred = developers.get("team1");
    // assertEquals("Fred", fred.getName());
    // assertEquals("creator", fred.getRole());
    // }

    public static enum Color {
        WHITE, BLACK, RED;
    }

    public static class MapBean2 {
        private Map<Developer2, Color> data;
        private String name;
        private Map<Color, Developer2> developers;

        public MapBean2() {
            name = "Bean123";
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<Color, Developer2> getDevelopers() {
            return developers;
        }

        public void setDevelopers(Map<Color, Developer2> developers) {
            this.developers = developers;
        }

        public Map<Developer2, Color> getData() {
            return data;
        }

        public void setData(Map<Developer2, Color> data) {
            this.data = data;
        }

    }

    public static class Developer2 implements Comparable<Developer2> {
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

        public int compareTo(Developer2 o) {
            return name.compareTo(o.name);
        }
    }

}
