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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

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
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-12.yaml");
        assertEquals(etalon, output);
    }

    public void testMap2() {
        MapBean2 bean = new MapBean2();
        Map<Developer2, Color> data = new LinkedHashMap<Developer2, Color>();
        data.put(new Developer2("Andy", "tester"), Color.BLACK);
        data.put(new SuperMan("Bill", "cleaner", false), Color.BLACK);
        data.put(new Developer2("Lisa", "owner"), Color.RED);
        bean.setData(data);
        Map<Color, Developer2> developers = new LinkedHashMap<Color, Developer2>();
        developers.put(Color.WHITE, new Developer2("Fred", "creator"));
        developers.put(Color.RED, new SuperMan("Jason", "contributor", true));
        developers.put(Color.BLACK, new Developer2("John", "committer"));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-13.yaml");
        assertEquals(etalon, output);
        // load
        Yaml beanLoader = new Yaml();
        MapBean2 parsed = beanLoader.loadAs(etalon, MapBean2.class);
        assertNotNull(parsed);
        Map<Developer2, Color> parsedData = parsed.getData();
        assertEquals(3, parsedData.size());
        assertTrue(parsedData.containsKey(new SuperMan("Bill", "cleaner", false)));
        assertEquals(Color.BLACK, parsedData.get(new SuperMan("Bill", "cleaner", false)));
        //
        Map<Color, Developer2> parsedDevelopers = parsed.getDevelopers();
        assertEquals(3, parsedDevelopers.size());
        assertEquals(new SuperMan("Jason", "contributor", true), parsedDevelopers.get(Color.RED));
    }

    public void testLoadMap() {
        String output = Util.getLocalResource("examples/map-bean-12.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        MapBean2 parsed = beanLoader.loadAs(output, MapBean2.class);
        assertNotNull(parsed);
        Map<Developer2, Color> data = parsed.getData();
        assertEquals(2, data.size());
        Iterator<Developer2> iter = data.keySet().iterator();
        Developer2 first = iter.next();
        assertEquals("Andy", first.getName());
        assertEquals("tester", first.getRole());
        assertEquals(Color.BLACK, data.get(first));
        Developer2 second = iter.next();
        assertEquals("Lisa", second.getName());
        assertEquals("owner", second.getRole());
        assertEquals(Color.RED, data.get(second));
        //
        Map<Color, Developer2> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        Iterator<Color> iter2 = developers.keySet().iterator();
        Color firstColor = iter2.next();
        assertEquals(Color.WHITE, firstColor);
        Developer2 dev1 = developers.get(firstColor);
        assertEquals("Fred", dev1.getName());
        assertEquals("creator", dev1.getRole());
        Color secondColor = iter2.next();
        assertEquals(Color.BLACK, secondColor);
        Developer2 dev2 = developers.get(secondColor);
        assertEquals("John", dev2.getName());
        assertEquals("committer", dev2.getRole());
    }

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

        private Developer2(String name, String role) {
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

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Developer2) {
                return toString().equals(obj.toString());
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
            return "Developer " + name + " " + role;
        }

    }

    public static class SuperMan extends Developer2 {
        private boolean smart;

        public SuperMan() {
            super();
        }

        private SuperMan(String name, String role, boolean smart) {
            super(name, role);
            this.smart = smart;
        }

        public boolean isSmart() {
            return smart;
        }

        public void setSmart(boolean smart) {
            this.smart = smart;
        }

        @Override
        public String toString() {
            return "Super" + super.toString();
        }
    }
}
