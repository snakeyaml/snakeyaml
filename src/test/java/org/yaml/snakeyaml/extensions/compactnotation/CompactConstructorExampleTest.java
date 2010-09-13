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

package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class CompactConstructorExampleTest extends TestCase {

    public void test1() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container1.yaml");
        Object obj = yaml.load(doc);
        assertNotNull(obj);
        assertEquals(new Container(), obj);
    }

    public void test2() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container2.yaml");
        Object obj = yaml.load(doc);
        assertNotNull(obj);
        assertEquals(new Container("title"), obj);
    }

    public void test3() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container3.yaml");
        Container obj = (Container) yaml.load(doc);
        assertNotNull(obj);
        assertEquals(new Container("title3"), obj);
        assertEquals("title3", obj.getTitle());
        assertEquals("parent", obj.getName());
        assertEquals("123", obj.getId());
    }

    public void test4() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container4.yaml");
        Object obj = yaml.load(doc);
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("title4"), obj);
        assertEquals("title4", container.getTitle());
        assertEquals("child4", container.getName());
        assertEquals("444", container.getId());
    }

    public void test5() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container5.yaml");
        Object obj = yaml.load(doc);
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("title4"), obj);
        assertEquals("title4", container.getTitle());
        assertEquals("child5", container.getName());
        assertEquals("ID555", container.getId());
    }

    public void test6() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container6.yaml");
        Object obj = yaml.load(doc);
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("title4"), obj);
        assertEquals("title4", container.getTitle());
        assertEquals("child6", container.getName());
        assertEquals("ID6", container.getId());
    }

    public void test7() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container7.yaml");
        Object obj = yaml.load(doc);
        // System.out.println(obj);
        Container container = (Container) obj;
        assertNotNull(obj);
        assertEquals(new Container("The title"), obj);
        assertEquals("The title", container.getTitle());
        assertEquals("child7", container.getName());
        assertEquals("id7", container.getId());
    }

    public void test8() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container8.yaml");
        try {
            yaml.load(doc);
            fail();
        } catch (Exception e) {
            assertEquals(
                    "org.yaml.snakeyaml.error.YAMLException: Unable to find property 'nonsense' on class: org.yaml.snakeyaml.extensions.compactnotation.Container",
                    e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void test9() {
        CompactConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/container9.yaml");
        Map<String, Object> map = (Map<String, Object>) yaml.load(doc);
        assertEquals(1, map.size());
        Map<String, Container> containers = (Map<String, Container>) map.get("something");
        // System.out.println(obj);
        // TODO
        // assertEquals(2, containers.size());
        // for (Container c : containers.values()) {
        // assertTrue(c.getId().matches("id\\d"));
        // }
    }
}
