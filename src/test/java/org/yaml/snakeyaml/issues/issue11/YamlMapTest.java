/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue11;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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

public class YamlMapTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testYamlMap() throws IOException {
        Map<String, Object> data = new TreeMap<String, Object>();
        data.put("key3", new Custom("test"));
        data.put("key4", new Wrapper("test", new Custom("test")));

        Yaml yaml = new Yaml(new Loader(new ExtendedConstructor()), new Dumper(
                new ExtendedRepresenter(), new DumperOptions()));
        String output = yaml.dump(data);
        // System.out.println(output);
        Object o = yaml.load(output);

        assertTrue(o instanceof Map);
        Map<String, Object> m = (Map<String, Object>) o;
        assertTrue(m.get("key3") instanceof Custom);
        assertTrue(m.get("key4") instanceof Wrapper);
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

        public Custom(String s) {
            str = s;
        }

        public Custom(Integer i) {
            str = "";
        }

        public Custom(Custom c) {
            str = c.str;
        }

        public String toString() {
            return str;
        }
    }

    public static class ExtendedRepresenter extends Representer {
        public ExtendedRepresenter() {
            this.representers.put(Custom.class, new RepresentCustom());
        }

        private class RepresentCustom implements Represent {
            public Node representData(Object data) {
                return representScalar("!Custom", ((Custom) data).toString());
            }
        }
    }

    public static class ExtendedConstructor extends Constructor {
        public ExtendedConstructor() {
            this.yamlConstructors.put("!Custom", new ConstructCustom());
        }

        private class ConstructCustom extends AbstractConstruct {
            public Object construct(Node node) {
                String str = (String) constructScalar((ScalarNode) node);
                return new Custom(str);
            }

        }
    }
}
