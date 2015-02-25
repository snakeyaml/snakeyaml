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
package org.yaml.snakeyaml.resolver;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class ResolverTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testAddImplicitResolver() {
        Yaml yaml = new Yaml(new MyConstructor(), new MyRepresenter());
        Pattern regexp = Pattern.compile("\\d\\d-\\d\\d-\\d\\d\\d");
        yaml.addImplicitResolver(new Tag(Tag.PREFIX + "Phone"), regexp, "0123456789");
        Phone phone1 = new Phone("12-34-567");
        Phone phone2 = new Phone("11-22-333");
        Phone phone3 = new Phone("44-55-777");
        List<Phone> etalonList = new ArrayList<Phone>();
        etalonList.add(phone1);
        etalonList.add(phone2);
        etalonList.add(phone3);
        String output = yaml.dump(etalonList);
        assertEquals("[12-34-567, 11-22-333, 44-55-777]\n", output);
        List<Phone> parsedList = (List<Phone>) yaml.load(output);
        assertEquals(3, parsedList.size());
        assertEquals(phone1, parsedList.get(0));
        assertEquals(phone2, parsedList.get(1));
        assertEquals(phone3, parsedList.get(2));
        assertEquals(etalonList, parsedList);
    }

    public void testAddImplicitResolver2() {
        Yaml yaml = new Yaml(new PointRepresenter());
        Pattern regexp = Pattern.compile("\\d\\d-\\d\\d-\\d\\d\\d");
        yaml.addImplicitResolver(new Tag(Tag.PREFIX + "Phone"), regexp, "\0");
        Pattern regexp2 = Pattern.compile("x\\d_y\\d");
        // try any scalar, and not only those which start with 'x'
        yaml.addImplicitResolver(new Tag(Tag.PREFIX + "Point"), regexp2, null);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("a", new Phone("12-34-567"));
        map.put("b", new Point(1, 5));
        String output = yaml.dump(map);
        assertEquals("{a: 12-34-567, b: x1_y5}\n", output);
    }

    class Phone {
        private String number;

        public Phone(String n) {
            this.number = n;
        }

        public String getNumber() {
            return number;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Phone)) {
                return false;
            }
            return toString().equals(obj.toString());
        }

        @Override
        public String toString() {
            return "Phone: " + number;
        }
    }

    class MyRepresenter extends Representer {
        public MyRepresenter() {
            this.representers.put(Phone.class, new RepresentPhone());
        }

        private class RepresentPhone implements Represent {
            public Node representData(Object data) {
                Phone phone = (Phone) data;
                String value = phone.getNumber();
                return representScalar(new Tag(Tag.PREFIX + "Phone"), value);
            }
        }
    }

    class MyConstructor extends Constructor {
        public MyConstructor() {
            this.yamlConstructors.put(new Tag(Tag.PREFIX + "Phone"), new ConstructPhone());
        }

        private class ConstructPhone extends AbstractConstruct {
            public Object construct(Node node) {
                String val = (String) constructScalar((ScalarNode) node);
                return new Phone(val);
            }
        }
    }

    class PointRepresenter extends Representer {
        public PointRepresenter() {
            this.representers.put(Point.class, new RepresentPoint());
            this.representers.put(Phone.class, new RepresentPhone());
        }

        private class RepresentPoint implements Represent {
            public Node representData(Object data) {
                Point phone = (Point) data;
                String value = "x" + (int) phone.getX() + "_y" + (int) phone.getY();
                return representScalar(new Tag(Tag.PREFIX + "Point"), value);
            }
        }

        private class RepresentPhone implements Represent {
            public Node representData(Object data) {
                Phone phone = (Phone) data;
                String value = phone.getNumber();
                return representScalar(new Tag(Tag.PREFIX + "Phone"), value);
            }
        }
    }

}
