package org.yaml.snakeyaml.immutable;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;

public abstract class ShapeCustomConstructorTest extends TestCase {

    public void testShape() {
        Shape shape = new Shape();
        shape.setColor(new Color("RED"));
        shape.setId(new Integer(23));
        shape.setPoint(new Point(1.1, 3.14));
        shape.setPoint3d(new Point3d(new Point(1.7, 56.0), 2.9));
        JavaBeanDumper dumper = new JavaBeanDumper();
        String output = dumper.dump(shape);
        System.out.println(output);
        Loader loader = new Loader(new ShapeConstructor());
        Yaml yaml = new Yaml(loader);
        Shape loaded = (Shape) yaml.load(output);
    }

    private class ShapeConstructor extends Constructor {
        public ShapeConstructor() {
            rootType = Shape.class;
        }

        @Override
        protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
            if (node.getType().equals(Color.class)) {
                node.setTag("tag:yaml.org,2002:map");
                node.setType(Object.class);
                Map<Object, Object> colorMap = constructMapping(node);
                Color color = new Color((String) colorMap.get("name"));
                return color;
            } else {

            }
            return super.constructJavaBean2ndStep(node, object);
        }

    }
}
