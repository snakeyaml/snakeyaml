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

package examples.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.SnakeYaml;
import org.yaml.snakeyaml.Util;

/**
 * Test different Map implementations as JavaBean properties
 */
public class TypeSafeMapImplementationsTest extends TestCase {
    public void testDumpMap() {
        MapBean bean = new MapBean();
        SortedMap<String, String> sortedMap = new TreeMap<String, String>();
        sortedMap.put("2", "two");
        sortedMap.put("1", "one");
        bean.setSorted(sortedMap);
        Properties props = new Properties();
        props.setProperty("key1", "value1");
        props.setProperty("key2", "value2");
        bean.setProperties(props);
        JavaBeanDumper dumper = new JavaBeanDumper(false);
        String output = dumper.dump(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-1.yaml");
        assertEquals(etalon, output);
    }

    public void testLoadMap() {
        String output = Util.getLocalResource("examples/map-bean-1.yaml");
        // System.out.println(output);
        JavaBeanLoader<MapBean> beanLoader = new JavaBeanLoader<MapBean>(MapBean.class);
        MapBean parsed = beanLoader.load(output);
        assertNotNull(parsed);
        SortedMap<String, String> sortedMap = parsed.getSorted();
        assertEquals(2, sortedMap.size());
        assertEquals("one", sortedMap.get("1"));
        assertEquals("two", sortedMap.get("2"));
        String first = sortedMap.keySet().iterator().next();
        assertEquals("1", first);
        //
        Properties props = parsed.getProperties();
        assertEquals(2, props.size());
        assertEquals("value1", props.getProperty("key1"));
        assertEquals("value2", props.getProperty("key2"));
    }

    public static class MapBean {
        private SortedMap<String, String> sorted;
        private Properties properties;
        private String name;

        public MapBean() {
            name = "Bean123";
        }

        public SortedMap<String, String> getSorted() {
            return sorted;
        }

        public void setSorted(SortedMap<String, String> sorted) {
            this.sorted = sorted;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @SuppressWarnings("unchecked")
    public void testNoJavaBeanMap() {
        List<Object> list = new ArrayList<Object>(3);
        SortedMap<String, String> sortedMap = new TreeMap<String, String>();
        sortedMap.put("2", "two");
        sortedMap.put("1", "one");
        list.add(sortedMap);
        Properties props = new Properties();
        props.setProperty("key1", "value1");
        props.setProperty("key2", "value2");
        list.add(props);
        list.add("aaa");
        SnakeYaml yaml = new SnakeYaml();
        String output = yaml.dump(list);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-bean-2.yaml");
        assertEquals(etalon, output);
        // load
        List<Object> list2 = (List<Object>) yaml.load(output);
        assertEquals(3, list2.size());
        Map map1 = (Map) list.get(0);// it was SortedMap
        assertEquals(2, map1.size());
        assertEquals("one", map1.get("1"));
        assertEquals("two", map1.get("2"));
        Map map2 = (Map) list.get(1);// it was Properties
        assertEquals(2, map2.size());
        assertEquals("value1", map2.get("key1"));
        assertEquals("value2", map2.get("key2"));
        assertEquals("aaa", list.get(2));
    }

    @SuppressWarnings("unchecked")
    public void testRecursiveNoJavaBeanMap1() {
        SortedMap<String, Object> sortedMap = new TreeMap<String, Object>();
        sortedMap.put("2", "two");
        sortedMap.put("1", "one");
        sortedMap.put("3", sortedMap);
        SnakeYaml yaml = new SnakeYaml();
        String output = yaml.dump(sortedMap);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-recursive-1.yaml");
        assertEquals(etalon, output);
        // load with different order
        Map map1 = (Map) yaml.load(Util.getLocalResource("examples/map-recursive-1_1.yaml"));
        assertEquals(3, map1.size());
        assertEquals("one", map1.get("1"));
        assertEquals("two", map1.get("2"));
        // test that the order is taken from YAML instead of sorting
        String first = (String) map1.keySet().iterator().next();
        assertEquals("2", first);
    }

    @SuppressWarnings("unchecked")
    public void testRecursiveNoJavaBeanProperties2() {
        Properties props = new Properties();
        props.setProperty("key1", "value1");
        props.setProperty("key2", "value2");
        Map map = props;
        map.put("key3", props);
        SnakeYaml yaml = new SnakeYaml();
        String output = yaml.dump(props);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/map-recursive-2.yaml");
        assertEquals(etalon, output);
        // load
        Map map2 = (Map) yaml.load(output);
        assertEquals(3, map2.size());
        assertEquals("value1", map2.get("key1"));
        assertEquals("value2", map2.get("key2"));
    }

    @SuppressWarnings("unchecked")
    public void testRecursiveNoJavaBeanMap3() {
        SnakeYaml yaml = new SnakeYaml();
        String output = Util.getLocalResource("examples/map-recursive-3.yaml");
        // System.out.println(output);
        SortedMap map1 = (SortedMap) yaml.load(output);
        assertEquals(3, map1.size());
        assertEquals("one", map1.get("1"));
        assertEquals("two", map1.get("2"));
        // test that the order is NOT taken from YAML but sorted
        String first = (String) map1.keySet().iterator().next();
        assertEquals("1", first);
    }

    public void testRecursiveNoJavaBeanProperties4() {
        SnakeYaml yaml = new SnakeYaml();
        String output = Util.getLocalResource("examples/map-recursive-4.yaml");
        // System.out.println(output);
        try {
            yaml.load(output);
            fail("Recursive Properties are not supported.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Properties must not be recursive."));
        }
    }
}
