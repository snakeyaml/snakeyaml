/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

public class SafeConstructorTest extends TestCase {

    public void testConstructFloat() {
        Yaml yaml = new Yaml();
        assertEquals(3.1416, yaml.load("+3.1416"));
        assertEquals(Double.POSITIVE_INFINITY, yaml.load("+.inf"));
        assertEquals(Double.POSITIVE_INFINITY, yaml.load(".inf"));
        assertEquals(Double.NEGATIVE_INFINITY, yaml.load("-.inf"));
    }

    public void testSafeConstruct() {
        Yaml yaml = new Yaml(new Loader(new SafeConstructor()));
        assertEquals(3.1416, yaml.load("+3.1416"));
    }

    public void testSafeConstructJavaBean() {
        Yaml yaml = new Yaml(new Loader(new SafeConstructor()));
        String data = "--- !!org.yaml.snakeyaml.constructor.Person\nfirstName: Andrey\nage: 99";
        try {
            yaml.load(data);
            fail("JavaBeans cannot be created by SafeConstructor.");
        } catch (ConstructorException e) {
            assertTrue(e
                    .getMessage()
                    .contains(
                            "could not determine a constructor for the tag tag:yaml.org,2002:org.yaml.snakeyaml.constructor.Person"));
        }
    }
}
