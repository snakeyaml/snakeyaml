package org.yaml.snakeyaml.immutable;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class ShapeCustomDumperTest extends TestCase {

    public void testColor() {
        Yaml yaml = new Yaml();
        Color loaded = (Color) yaml.load("!!org.yaml.snakeyaml.immutable.Color BLACK");
        assertEquals("BLACK", loaded.getName());
    }

    public void testCode() {
        Yaml yaml = new Yaml();
        Code loaded = (Code) yaml.load("!!org.yaml.snakeyaml.immutable.Code 123");
        assertEquals(new Integer(123), loaded.getCode());
    }

    public void testSuperColorFail() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("!!org.yaml.snakeyaml.immutable.SuperColor BLACK");
            fail("SuperColor requires Color and not a String.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.immutable.SuperColor; exception=Unsupported class: class org.yaml.snakeyaml.immutable.Color",
                    e.getMessage());
        }
    }

    public void testPoint() {
        Yaml yaml = new Yaml();
        Point loaded = (Point) yaml.load("!!org.yaml.snakeyaml.immutable.Point [1.17, 3.14]");
        assertEquals(1.17, loaded.getX());
        assertEquals(3.14, loaded.getY());
    }

    public void testPointBlock() {
        Yaml yaml = new Yaml();
        Point loaded = (Point) yaml.load("!!org.yaml.snakeyaml.immutable.Point\n- 1.17\n- 3.14");
        assertEquals(1.17, loaded.getX());
        assertEquals(3.14, loaded.getY());
    }

    public void testPoint3d() {
        Yaml yaml = new Yaml();
        Point3d loaded = (Point3d) yaml
                .load("!!org.yaml.snakeyaml.immutable.Point3d [!!org.yaml.snakeyaml.immutable.Point [1.17, 3.14], 345.1]");
        assertEquals(345.1, loaded.getZ());
    }

    public void testShape() {
        Yaml yaml = new Yaml();
        String source = Util.getLocalResource("immutable/shape1.yaml");
        System.out.println(source);
        Shape loaded = (Shape) yaml.load(source);
        assertEquals(new Integer(123), loaded.getId());
    }

}
