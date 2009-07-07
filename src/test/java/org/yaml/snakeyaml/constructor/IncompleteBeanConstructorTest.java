/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class IncompleteBeanConstructorTest extends TestCase {

    public void testRepresentor() throws IOException {
        IncompleteJavaBean bean = new IncompleteJavaBean();
        Yaml yaml = new Yaml();
        String output = yaml.dump(bean);
        String className = this.getClass().getPackage().getName();
        assertEquals("!!" + className + ".IncompleteJavaBean {name: No name}\n", output);
    }

    public void testConstructor() throws IOException {
        String className = "!!" + this.getClass().getPackage().getName()
                + ".IncompleteJavaBean {number: 2}";
        Yaml yaml = new Yaml();
        IncompleteJavaBean bean = (IncompleteJavaBean) yaml.load(className);
        assertNotNull(bean);
        assertEquals("No name", bean.getName());
        assertEquals(2, bean.obtainNumber());
    }

    public void testConstructor2() throws IOException {
        String className = "!!" + this.getClass().getPackage().getName()
                + ".IncompleteJavaBean {number: 2, name: Bill}";
        Yaml yaml = new Yaml();
        try {
            yaml.load(className);
            fail("'name' property does not have setter.");
        } catch (YAMLException e) {
            assertTrue(e.getMessage(), e.getMessage().contains(
                    "IncompleteJavaBean does not have the write method"));
        }
    }
}
