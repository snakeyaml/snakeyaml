/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import junit.framework.TestCase;

public class YamlTest extends TestCase {

    public void testSetNoName() {
        Yaml yaml = new Yaml();
        assertTrue(yaml.toString().matches("Yaml:\\d+"));
    }

    public void testSetName() {
        Yaml yaml = new Yaml();
        yaml.setName("REST");
        assertEquals("REST", yaml.getName());
        assertEquals("REST", yaml.toString());
    }
}
