package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

public class MappingNodeTest extends TestCase {

    public void testNullValue() {
        try {
            new MappingNode("!tag", null, null, null, false);
            fail("Value is required.");
        } catch (Exception e) {
            assertEquals("value in a Node is required.", e.getMessage());
        }
    }
}
