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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

/**
 * Test different Map implementations as JavaBean properties
 */
public class TypeSafeSetImplementationsTest extends TestCase {
    public void testDumpSet() {
        SetBean bean = new SetBean();
        SortedSet<String> sortedSet = new TreeSet<String>();
        sortedSet.add("two");
        sortedSet.add("one");
        sortedSet.add("three");
        bean.setSorted(sortedSet);
        SortedSet<Developer> developers = new TreeSet<Developer>();
        developers.add(new Developer("John", "founder"));
        developers.add(new Developer("Karl", "user"));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/set-bean-1.yaml");
        assertEquals(etalon, output);
    }

    public void testDumpSet2() {
        SetBean bean = new SetBean();
        SortedSet<String> sortedSet = new TreeSet<String>();
        sortedSet.add("two");
        sortedSet.add("one");
        sortedSet.add("three");
        bean.setSorted(sortedSet);
        SortedSet<Developer> developers = new TreeSet<Developer>();
        developers.add(new Developer("John", "founder"));
        developers.add(new Developer("Karl", "user"));
        developers.add(new SuperDeveloper("Bill", "super"));
        bean.setDevelopers(developers);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        String etalon = Util.getLocalResource("examples/set-bean-6.yaml");
        assertEquals(etalon, output);
    }

    public void testLoadSet() {
        String output = Util.getLocalResource("examples/set-bean-1.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        SetBean parsed = beanLoader.loadAs(output, SetBean.class);
        assertNotNull(parsed);
        SortedSet<String> sortedMap = parsed.getSorted();
        assertEquals(3, sortedMap.size());
        assertTrue(sortedMap.contains("one"));
        assertTrue(sortedMap.contains("two"));
        assertTrue(sortedMap.contains("three"));
        String first = sortedMap.iterator().next();
        assertEquals("one", first);
        //
        SortedSet<Developer> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        assertEquals("John", developers.first().getName());
        assertEquals("Karl", developers.last().getName());
    }

    public void testLoadSetReversed() {
        String output = Util.getLocalResource("examples/set-bean-2.yaml");
        // System.out.println(output);
        Yaml beanLoader = new Yaml();
        SetBean parsed = beanLoader.loadAs(output, SetBean.class);
        assertNotNull(parsed);
        SortedSet<String> sortedMap = parsed.getSorted();
        assertEquals(3, sortedMap.size());
        assertTrue(sortedMap.contains("one"));
        assertTrue(sortedMap.contains("two"));
        assertTrue(sortedMap.contains("three"));
        // alphabetically: one, three, two
        assertEquals("one", sortedMap.first());
        assertEquals("two", sortedMap.last());
        // the order is not from YAML (must be sorted)
        SortedSet<Developer> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        assertEquals("John", developers.first().getName());
        assertEquals("Karl", developers.last().getName());
    }

    public static class SetBean {
        private SortedSet<String> sorted;
        private SortedSet<Developer> developers;
        private String name;

        public SetBean() {
            name = "Bean123";
        }

        public SortedSet<String> getSorted() {
            return sorted;
        }

        public void setSorted(SortedSet<String> sorted) {
            this.sorted = sorted;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SortedSet<Developer> getDevelopers() {
            return developers;
        }

        public void setDevelopers(SortedSet<Developer> developers) {
            this.developers = developers;
        }
    }

    public static class Developer implements Comparable<Developer> {
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

        public int compareTo(Developer o) {
            return name.compareTo(o.name);
        }
    }

    public static class SuperDeveloper extends Developer {

        public SuperDeveloper() {
            super();
        }

        public SuperDeveloper(String string, String string2) {
            super(string, string2);
        }

    }

    @SuppressWarnings("unchecked")
    public void testNoJavaBeanSetRecursive() {
        Set<Object> set = new HashSet<Object>(3);
        set.add("aaa");
        set.add(111);
        Box box = new Box();
        box.setId("id123");
        box.setSet(set);
        set.add(box);
        Yaml yaml = new Yaml();
        String output = yaml.dump(set);
        // System.out.println(output);
        // the order may differ on different JVMs
        // String etalon = Util.getLocalResource("examples/set-bean-3.yaml");
        // assertEquals(etalon, output);
        assertTrue(output.contains("&id001 !!set"));
        assertTrue(output.contains("? !!examples.collections.TypeSafeSetImplementationsTest$Box"));
        assertTrue(output.contains("set: *id001"));
        assertTrue(output.contains("111: null"));
        // load
        Set<Object> list2 = (Set<Object>) yaml.load(output);
        assertEquals(3, list2.size());
        assertTrue(list2.contains("aaa"));
        assertTrue(list2.contains(111));
    }

    public static class Box {
        private String id;
        private Set<Object> set;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Set<Object> getSet() {
            return set;
        }

        public void setSet(Set<Object> set) {
            this.set = set;
        }
    }

    @SuppressWarnings("unchecked")
    public void testNoJavaBeanSet() {
        Yaml yaml = new Yaml();
        String output = Util.getLocalResource("examples/set-bean-4.yaml");
        // System.out.println(output);
        // load
        Set<String> set = (Set<String>) yaml.load(output);
        assertEquals(3, set.size());
        assertTrue(set.contains("aaa"));
        assertTrue(set.contains("bbb"));
        assertTrue(set.contains("zzz"));
        Iterator<String> iter = set.iterator();
        assertEquals("bbb", iter.next());
        assertEquals("aaa", iter.next());
        assertEquals("zzz", iter.next());
    }

    @SuppressWarnings("unchecked")
    public void testNoJavaBeanSet2() {
        Yaml yaml = new Yaml();
        String output = Util.getLocalResource("examples/set-bean-5.yaml");
        // System.out.println(output);
        // load and sort
        Set<String> set = (Set<String>) yaml.load(output);
        assertEquals(3, set.size());
        assertTrue(set.contains("aaa"));
        assertTrue(set.contains("bbb"));
        assertTrue(set.contains("zzz"));
        Iterator<String> iter = set.iterator();
        assertEquals("aaa", iter.next());
        assertEquals("bbb", iter.next());
        assertEquals("zzz", iter.next());
    }
}
