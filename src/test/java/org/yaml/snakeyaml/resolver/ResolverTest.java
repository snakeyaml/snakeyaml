/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.resolver;

import java.awt.Point;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class ResolverTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testAddImplicitResolver() {
        Dumper dumper = new Dumper(new MyRepresenter(), new DumperOptions());
        Loader loader = new Loader(new MyConstructor());
        Yaml yaml = new Yaml(loader, dumper);
        Pattern regexp = Pattern.compile("\\d\\d-\\d\\d-\\d\\d\\d");
        yaml.addImplicitResolver("tag:yaml.org,2002:Phone", regexp, "0123456789");
        Phone phone1 = new Phone("12-34-567");
        Phone phone2 = new Phone("11-22-333");
        Phone phone3 = new Phone("44-55-777");
        List<Phone> etalonList = new LinkedList<Phone>();
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
        Dumper dumper = new Dumper(new PointRepresenter(), new DumperOptions());
        Yaml yaml = new Yaml(dumper);
        Pattern regexp = Pattern.compile("\\d\\d-\\d\\d-\\d\\d\\d");
        yaml.addImplicitResolver("tag:yaml.org,2002:Phone", regexp, "\0");
        Pattern regexp2 = Pattern.compile("x\\d_y\\d");
        // try any scalar, and not only those which start with 'x'
        yaml.addImplicitResolver("tag:yaml.org,2002:Point", regexp2, null);
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
                return representScalar("tag:yaml.org,2002:Phone", value);
            }
        }
    }

    class MyConstructor extends Constructor {
        public MyConstructor() {
            this.yamlConstructors.put("tag:yaml.org,2002:Phone", new ConstructPhone());
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
                return representScalar("tag:yaml.org,2002:Point", value);
            }
        }

        private class RepresentPhone implements Represent {
            public Node representData(Object data) {
                Phone phone = (Phone) data;
                String value = phone.getNumber();
                return representScalar("tag:yaml.org,2002:Phone", value);
            }
        }
    }

    /**
     * Parse scalars as Strings
     */
    @SuppressWarnings("unchecked")
    public void testStringResolver() {
        Yaml yaml = new Yaml(new Loader(), new Dumper(new DumperOptions()), new Resolver(false));
        List<Object> output = (List<Object>) yaml.load("[ '1.00', 1.00, !!float '1.00' ]");
        assertEquals("1.00", output.get(0));
        assertEquals("1.00", output.get(1));
        assertEquals(1.0, output.get(2));
    }
}
