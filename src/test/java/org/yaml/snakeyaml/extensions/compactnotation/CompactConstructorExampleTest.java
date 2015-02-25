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
package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class CompactConstructorExampleTest extends TestCase {

    private Object load(String fileName) {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/" + fileName);
        Object obj = yaml.load(doc);
        assertNotNull(obj);
        return obj;
    }

    public void test1() {
        Object obj = load("example1.yaml");
        assertEquals(new Container(), obj);
    }

    public void test2() {
        Object obj = load("example2.yaml");
        assertEquals(new Container("title"), obj);
    }

    public void test3() {
        Container obj = (Container) load("example3.yaml");
        assertEquals(new Container("title3"), obj);
        assertEquals("title3", obj.getTitle());
        assertEquals("parent", obj.getName());
        assertEquals("123", obj.getId());
    }

    public void test4() {
        Object obj = load("example4.yaml");
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("title4"), obj);
        assertEquals("title4", container.getTitle());
        assertEquals("child4", container.getName());
        assertEquals("444", container.getId());
    }

    public void test5() {
        Object obj = load("example5.yaml");
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("title4"), obj);
        assertEquals("title4", container.getTitle());
        assertEquals("child5", container.getName());
        assertEquals("ID555", container.getId());
    }

    public void test6() {
        Object obj = load("example6.yaml");
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("title4"), obj);
        assertEquals("title4", container.getTitle());
        assertEquals("child6", container.getName());
        assertEquals("ID6", container.getId());
    }

    public void test7() {
        Object obj = load("example7.yaml");
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("The title"), obj);
        assertEquals("The title", container.getTitle());
        assertEquals("child7", container.getName());
        assertEquals("id7", container.getId());
    }

    @SuppressWarnings("unchecked")
    // TODO it is unclear how the result should look like for CON
    public void test9() {
        Map<String, Object> map = (Map<String, Object>) load("example9.yaml");
        assertEquals(1, map.size());
        Map<Container, Map<String, String>> containers = (Map<Container, Map<String, String>>) map
                .get("something");
        // System.out.println(obj);
        assertEquals(2, containers.size());
        for (Container c : containers.keySet()) {
            assertTrue(c.getId().matches("id\\d"));
            assertEquals(1, containers.get(c).size());
        }
    }

    @SuppressWarnings("unchecked")
    public void test10() {
        Map<String, Object> map = (Map<String, Object>) load("example10.yaml");
        assertEquals(1, map.size());
        List<Container> containers = (List<Container>) map.get("something");
        // System.out.println(obj);
        assertEquals(3, containers.size());
        for (Container c : containers) {
            assertTrue(c.toString(), c.getId().matches("id\\d+"));
            assertTrue(c.toString(), c.getName().matches("child\\d+"));
            // System.out.println(c);
        }
    }

    public void test11withoutPackageNames() {
        Constructor compact = new PackageCompactConstructor(
                "org.yaml.snakeyaml.extensions.compactnotation");
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/example11.yaml");
        Box box = (Box) yaml.load(doc);
        assertNotNull(box);
        assertEquals("id11", box.getId());
        assertEquals("Main box", box.getName());
        Item top = box.getTop();
        assertEquals("id003", top.getId());
        assertEquals("25.0", top.getPrice());
        assertEquals("parrot", top.getName());
        Item bottom = box.getBottom();
        assertEquals("id004", bottom.getId());
        assertEquals("3.5", bottom.getPrice());
        assertEquals("sweet", bottom.getName());
    }

    public void test12withList() {
        Constructor compact = new TableCompactConstructor(
                "org.yaml.snakeyaml.extensions.compactnotation");
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/example12.yaml");
        Table table = (Table) yaml.load(doc);
        assertNotNull(table);
        assertEquals("id12", table.getId());
        assertEquals("A table", table.getName());
        List<Row> rows = table.getRows();
        assertEquals(3, rows.size());
        Iterator<Row> iter = rows.iterator();
        Row first = iter.next();
        assertEquals("id111", first.getId());
        assertEquals("I think; therefore I am.", first.getDescription());
        assertEquals(0.125, first.getRatio(), 0.000000001);
        assertEquals(15, first.getSize());
        Row second = iter.next();
        assertEquals("id222", second.getId());
        assertEquals("We do not need new lines here, just replace them all with spaces\n",
                second.getDescription());
        assertEquals(0.333, second.getRatio(), 0.000000001);
        assertEquals(17, second.getSize());
        Row third = iter.next();
        assertEquals("id333", third.getId());
        assertEquals(
                "Please preserve all\nthe lines because they may be\nimportant, but do not include the last one !!!",
                third.getDescription());
        assertEquals(0.88, third.getRatio(), 0.000000001);
        assertEquals(52, third.getSize());
    }
}
