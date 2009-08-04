/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class ShapeImmutableTest extends TestCase {

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

    public void testSuperColor() {
        Yaml yaml = new Yaml();
        SuperColor superColor = (SuperColor) yaml
                .load("!!org.yaml.snakeyaml.immutable.SuperColor [!!org.yaml.snakeyaml.immutable.Color BLACK]");
        assertEquals("BLACK", superColor.getColor().getName());
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

    public void testCode2() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("!!org.yaml.snakeyaml.immutable.Code2 555");
            fail("There must be only 1 constructor with 1 argument for scalar.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.immutable.Code2; exception=More then 1 constructor with 1 argument found for class org.yaml.snakeyaml.immutable.Code2",
                    e.getMessage());
        }
    }

    public void testCode3() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("!!org.yaml.snakeyaml.immutable.Code3 777");
            fail("There must be 1 constructor with 1 argument for scalar.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.immutable.Code3; exception=No single argument constructor found for class org.yaml.snakeyaml.immutable.Code3",
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

    public void testPointOnlyOneArgument() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("!!org.yaml.snakeyaml.immutable.Point\n- 1.17");
            fail("Two arguments required.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.immutable.Point; exception=No constructors with 1 arguments found for class org.yaml.snakeyaml.immutable.Point",
                    e.getMessage());
        }
    }

    public void testPoint2() {
        Yaml yaml = new Yaml();
        Point2 loaded = (Point2) yaml.load("!!org.yaml.snakeyaml.immutable.Point2\n- 1\n- 3");
        assertEquals(new Integer(1), loaded.getX());
        assertEquals(new Integer(3), loaded.getY());
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
        Shape loaded = (Shape) yaml.load(source);
        assertEquals(new Integer(123), loaded.getId());
    }

    public void testShapeNoTags() {
        String source = Util.getLocalResource("immutable/shapeNoTags.yaml");
        JavaBeanLoader<Shape> beanLoader = new JavaBeanLoader<Shape>(Shape.class);
        Shape loaded = beanLoader.load(source);
        assertEquals(new Integer(123), loaded.getId());
        assertEquals("BLACK", loaded.getColor().getName());
        assertEquals(1.17, loaded.getPoint().getX());
        assertEquals(3.14, loaded.getPoint().getY());
        assertEquals(345.1, loaded.getPoint3d().getZ());
        assertEquals(1.96, loaded.getPoint3d().getPoint().getX());
        assertEquals(1.78, loaded.getPoint3d().getPoint().getY());
    }

}
