/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

public class NodeTest extends TestCase {

    public void testNode() {
        try {
            new ScalarNode("!foo", null, null, null, '"');
            fail("Value must be required.");
        } catch (Exception e) {
            assertEquals("value in a Node is required.", e.getMessage());
        }
    }

    public void testSetTag() {
        try {
            ScalarNode node = new ScalarNode("!foo", "Value1", null, null, '"');
            node.setTag(null);
            fail("Value must be required.");
        } catch (Exception e) {
            assertEquals("tag in a Node is required.", e.getMessage());
        }
    }
}
