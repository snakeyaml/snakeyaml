/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.math.BigInteger;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class BeanConstructorTest extends TestCase {

    public void testPrimitivesConstructor() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean1.class));
        Yaml yaml = new Yaml(loader);
        String document = Util.getLocalResource("constructor/test-primitives1.yaml");
        System.out.println(document);
        TestBean1 result = (TestBean1) yaml.load(document);
        assertNotNull(result);
        assertEquals(new Byte((byte) 1), result.getByteClass());
        assertEquals((byte) -3, result.getBytePrimitive());
        assertEquals(new Short((short) 0), result.getShortClass());
        assertEquals((short) -13, result.getShortPrimitive());
        assertEquals(new Integer(5), result.getInteger());
        assertEquals(17, result.getIntPrimitive());
        assertEquals("the text", result.getText());
        assertEquals("13", result.getId());
        assertEquals(new Long(11111111111L), result.getLongClass());
        assertEquals(9999999999L, result.getLongPrimitive());
        assertEquals(Boolean.TRUE, result.getBooleanClass());
        assertTrue(result.isBooleanPrimitive());
        assertEquals(new Character('2'), result.getCharClass());
        assertEquals('#', result.getCharPrimitive());
        assertEquals(new BigInteger("1234567890123456789012345678901234567890"), result
                .getBigInteger());
        assertEquals(new Float(2), result.getFloatClass());
        assertEquals(new Float(3.1416), result.getFloatPrimitive());
        assertEquals(new Double(4), result.getDoubleClass());
        assertEquals(new Double(11200), result.getDoublePrimitive());
        assertEquals(1199836800000L, result.getDate().getTime());
        assertEquals("public", result.publicField);
        //
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yamlToDump = new Yaml(options);
        String output = yamlToDump.dump(result);
        System.out.println(output);
        TestBean1 result2 = (TestBean1) yaml.load(output);
        assertNotNull(result2);
        TestBean1 result3 = (TestBean1) new Yaml().load(output);
        assertNotNull(result3);
    }

    public void testNoClassConstructor() {
        try {
            new Loader(new Constructor((Class<? extends Object>) null));
            fail("Class must be provided.");
        } catch (NullPointerException e) {
            assertEquals("Root type must be provided.", e.getMessage());
        }
    }

    public void testNoClassConstructorString() throws ClassNotFoundException {
        try {
            new Loader(new Constructor((String) null));
            fail("Class must be provided.");
        } catch (NullPointerException e) {
            assertEquals("Root type must be provided.", e.getMessage());
        }
    }

    public void testNoClassConstructorEmptyString() throws ClassNotFoundException {
        try {
            new Loader(new Constructor(" "));
            fail("Class must be provided.");
        } catch (YAMLException e) {
            assertEquals("Root type must be provided.", e.getMessage());
        }
    }

    public void testCharacter() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean1.class));
        Yaml yaml = new Yaml(loader);
        String document = "charClass: id";
        try {
            yaml.load(document);
            fail("Only one char must be allowed.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "Invalid node Character: 'id'; length: 2"));
        }
        document = "charClass: #";
        try {
            yaml.load(document);
            fail("Only one char must be allowed.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "Invalid node Character: ''; length: 0"));
        }
        document = "charClass: ''";
        try {
            yaml.load(document);
            fail("Only one char must be allowed.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "Invalid node Character: ''; length: 0"));
        }
        document = "charClass:\n";
        try {
            yaml.load(document);
            fail("Only one char must be allowed.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "Invalid node Character: ''; length: 0"));
        }

    }

    public void testNoEmptyConstructor() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean2.class));
        Yaml yaml = new Yaml(loader);
        String document = "text: qwerty";
        try {
            yaml.load(document);
            fail("No empty constructor available");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("InstantiationException"));
        }
    }

    private class TestBean2 {
        private String text;

        public TestBean2(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public void testPrivateMethod() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean3.class));
        Yaml yaml = new Yaml(loader);
        String document = "text: qwerty";
        try {
            yaml.load(document);
            fail("Private method cannot be called.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("InstantiationException"));
        }
    }

    private class TestBean3 {
        private String text;

        public TestBean3() {
            setText("123");
        }

        public String getText() {
            return text;
        }

        private void setText(String text) {
            this.text = text;
        }
    }

    public void testKeyNotScalar() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean1.class));
        Yaml yaml = new Yaml(loader);
        String document = "[1, 2]: qwerty";
        try {
            yaml.load(document);
            fail("Keys must be scalars.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Keys must be scalars but found"));
        }
    }

    public void testInvalidKey() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean1.class));
        Yaml yaml = new Yaml(loader);
        String document = "something: qwerty";
        try {
            yaml.load(document);
            fail("Non-existing property must fail.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "Unable to find property 'something'"));
        }
    }

    public void testStaticField() throws IOException {
        Loader loader = new Loader(new Constructor(TestBean1.class));
        Yaml yaml = new Yaml(loader);
        String document = "staticInteger: 123";
        try {
            yaml.load(document);
            fail("Staic variables must not be used.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "Unable to find property 'staticInteger'"));
        }
    }

    public void testScalarContructor() throws IOException {
        Loader loader = new Loader(new Constructor(Parent1.class));
        Yaml yaml = new Yaml(loader);
        String document = "id: 123\nchild: 25";
        Parent1 parent = (Parent1) yaml.load(document);
        assertEquals("123", parent.getId());
        Child1 child = parent.getChild();
        assertEquals(new Integer(25), child.getCode());
    }
}
