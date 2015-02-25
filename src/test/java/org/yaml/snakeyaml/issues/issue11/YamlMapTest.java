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
package org.yaml.snakeyaml.issues.issue11;

import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class YamlMapTest extends TestCase {
    public void testYaml() {
        Yaml yaml = new Yaml(new ExtendedConstructor(), new ExtendedRepresenter());
        String output = yaml.dump(new Custom(123));
        // System.out.println(output);
        Custom o = (Custom) yaml.load(output);
        assertEquals("123", o.getStr());
    }

    @SuppressWarnings("unchecked")
    public void testYamlMap() {
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("customTag", new Custom(123));

        Yaml yaml = new Yaml(new ExtendedConstructor(), new ExtendedRepresenter());
        String output = yaml.dump(data);
        // System.out.println(output);
        Object o = yaml.load(output);

        assertTrue(o instanceof Map);
        Map<String, Object> m = (Map<String, Object>) o;
        assertTrue(m.get("customTag") instanceof Custom);
    }

    @SuppressWarnings("unchecked")
    public void testYamlMapBean() {
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("knownClass", new Wrapper("test", new Custom(456)));

        Yaml yaml = new Yaml(new ExtendedConstructor(), new ExtendedRepresenter());
        String output = yaml.dump(data);
        // System.out.println(output);
        Object o = yaml.load(output);

        assertTrue(o instanceof Map);
        Map<String, Object> m = (Map<String, Object>) o;
        assertEquals(Wrapper.class, m.get("knownClass").getClass());
    }

    public static class Wrapper {
        private String a;
        private Custom b;

        public Wrapper(String s, Custom bb) {
            a = s;
            b = bb;
        }

        public Wrapper() {
        }

        public String getA() {
            return a;
        }

        public void setA(String s) {
            a = s;
        }

        public Custom getB() {
            return b;
        }

        public void setB(Custom bb) {
            b = bb;
        }
    }

    public static class Custom {
        final private String str;

        public Custom(Integer i) {
            str = i.toString();
        }

        public Custom(Custom c) {
            str = c.str;
        }

        public String toString() {
            return str;
        }

        public String getStr() {
            return str;
        }
    }

    public static class ExtendedRepresenter extends Representer {
        public ExtendedRepresenter() {
            this.representers.put(Custom.class, new RepresentCustom());
        }

        private class RepresentCustom implements Represent {
            public Node representData(Object data) {
                return representScalar(new Tag("!Custom"), ((Custom) data).toString());
            }
        }
    }

    public static class ExtendedConstructor extends Constructor {
        public ExtendedConstructor() {
            this.yamlConstructors.put(new Tag("!Custom"), new ConstructCustom());
        }

        private class ConstructCustom extends AbstractConstruct {
            public Object construct(Node node) {
                String str = (String) constructScalar((ScalarNode) node);
                return new Custom(new Integer(str));
            }

        }
    }
}
