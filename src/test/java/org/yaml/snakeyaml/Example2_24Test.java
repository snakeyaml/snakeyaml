/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Test Example 2.24 from the YAML specification
 * 
 * @author py4fun
 * @see http://yaml.org/spec/1.1/
 */
public class Example2_24Test extends TestCase {
    class MyConstructor extends Constructor {
        public MyConstructor() {
            this.yamlConstructors.put("tag:clarkevans.com,2002:shape", new ConstructShape());
            this.yamlConstructors.put("tag:clarkevans.com,2002:circle", new ConstructCircle());
            this.yamlConstructors.put("tag:clarkevans.com,2002:line", new ConstructLine());
            this.yamlConstructors.put("tag:clarkevans.com,2002:label", new ConstructLabel());
        }

        private class ConstructShape implements Construct {
            @SuppressWarnings("unchecked")
            public Object construct(Node node) {
                SequenceNode snode = (SequenceNode) node;
                List<Entity> values = (List<Entity>) constructSequence(snode);
                Shape shape = new Shape(values);
                return shape;
            }
        }

        private class ConstructCircle implements Construct {
            @SuppressWarnings("unchecked")
            public Object construct(Node node) {
                MappingNode mnode = (MappingNode) node;
                Map values = (Map) constructMapping(mnode);
                Circle circle = new Circle((Map) values.get("center"), (Integer) values
                        .get("radius"));
                return circle;
            }
        }

        private class ConstructLine implements Construct {
            @SuppressWarnings("unchecked")
            public Object construct(Node node) {
                MappingNode mnode = (MappingNode) node;
                Map values = (Map) constructMapping(mnode);
                Line line = new Line((Map) values.get("start"), (Map) values.get("finish"));
                return line;
            }
        }

        private class ConstructLabel implements Construct {
            @SuppressWarnings("unchecked")
            public Object construct(Node node) {
                MappingNode mnode = (MappingNode) node;
                Map values = (Map) constructMapping(mnode);
                Label label = new Label((Map) values.get("start"), (Integer) values.get("color"),
                        (String) values.get("text"));
                return label;
            }
        }
    }

    class MyRepresenter extends Representer {
        public MyRepresenter() {
            this.representers.put(Shape.class, new RepresentShape());
            this.representers.put(Circle.class, new RepresentCircle());
            this.representers.put(Line.class, new RepresentLine());
            this.representers.put(Label.class, new RepresentLabel());
            this.representers.put(HexInteger.class, new RepresentHex());
        }

        private class RepresentShape implements Represent {
            public Node representData(Object data) {
                Shape shape = (Shape) data;
                List<Entity> value = shape.getEntities();
                return representSequence("!shape", value, Boolean.FALSE);
            }
        }

        private class RepresentCircle implements Represent {
            public Node representData(Object data) {
                Circle circle = (Circle) data;
                Map<String, Object> map = new TreeMap<String, Object>();
                map.put("center", circle.getCenter());
                map.put("radius", circle.getRadius());
                return representMapping("!circle", map, Boolean.FALSE);
            }
        }

        private class RepresentLine implements Represent {
            public Node representData(Object data) {
                Line line = (Line) data;
                Map<String, Object> map = new TreeMap<String, Object>();
                map.put("start", line.getStart());
                map.put("finish", line.getFinish());
                return representMapping("!line", map, Boolean.FALSE);
            }
        }

        private class RepresentLabel implements Represent {
            public Node representData(Object data) {
                Label label = (Label) data;
                Map<String, Object> map = new TreeMap<String, Object>();
                map.put("start", label.getStart());
                map.put("color", new HexInteger(label.getColor()));
                map.put("text", label.getText());
                return representMapping("!label", map, Boolean.FALSE);
            }
        }

        private class RepresentHex implements Represent {
            public Node representData(Object data) {
                HexInteger hex = (HexInteger) data;
                return representScalar("tag:yaml.org,2002:int", "0x"
                        + Integer.toHexString(hex.getColor()).toUpperCase(), null);
            }
        }
    }

    private class HexInteger {
        private Integer color;

        public HexInteger(Integer color) {
            this.color = color;
        }

        public Integer getColor() {
            return color;
        }
    }

    private class Shape {
        private List<Entity> entities;

        public List<Entity> getEntities() {
            return entities;
        }

        public Shape(List<Entity> entities) {
            this.entities = entities;
        }
    }

    private class Entity {
    }

    private class Circle extends Entity {
        private Map<String, Integer> center;
        private Integer radius;

        public Circle(Map<String, Integer> center, Integer radius) {
            this.center = center;
            this.radius = radius;
        }

        public Map<String, Integer> getCenter() {
            return center;
        }

        public Integer getRadius() {
            return radius;
        }
    }

    private class Line extends Entity {
        private Map<String, Integer> start;
        private Map<String, Integer> finish;

        public Line(Map<String, Integer> start, Map<String, Integer> finish) {
            this.start = start;
            this.finish = finish;
        }

        public Map<String, Integer> getStart() {
            return start;
        }

        public Map<String, Integer> getFinish() {
            return finish;
        }
    }

    private class Label extends Entity {
        private Map<String, Integer> start;
        private Integer color;
        private String text;

        public Label(Map<String, Integer> start, Integer color, String text) {
            this.start = start;
            this.color = color;
            this.text = text;
        }

        public Map<String, Integer> getStart() {
            return start;
        }

        public Integer getColor() {
            return color;
        }

        public String getText() {
            return text;
        }
    }

    public void testExample_2_24() throws IOException {
        Loader loader = new Loader(new MyConstructor());
        Yaml yaml = new Yaml(loader);
        Shape shape = (Shape) yaml.load(Util.getLocalResource("specification/example2_24.yaml"));
        assertNotNull(shape);
        Dumper dumper = new Dumper(new MyRepresenter(), new DumperOptions());
        yaml = new Yaml(dumper);
        String output = yaml.dump(shape);
        String etalon = Util.getLocalResource("specification/example2_24_dumped.yaml");
        assertEquals(etalon, output);
    }
}
