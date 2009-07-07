/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class MockDateBeanConstructorTest extends TestCase {

    public void testConstructor() throws IOException {
        String className = "!!" + this.getClass().getPackage().getName() + ".MockDate 2009-07-24";
        Yaml yaml = new Yaml();
        try {
            yaml.load(className);
            fail("MockDate cannot be constructed.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("MockDate"));
        }
    }
}
