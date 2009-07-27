/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import java.util.ArrayList;

import junit.framework.TestCase;

public class SequenceNodeTest extends TestCase {

    public void testGetNodeId() {
        SequenceNode node = new SequenceNode("!foo", new ArrayList<Node>(), null, null, true);
        assertEquals(NodeId.sequence, node.getNodeId());
    }

    public void testNullValue() {
        try {
            new SequenceNode("!foo", null, null, null, true);
            fail("Value is required.");
        } catch (Exception e) {
            assertEquals("value in a Node is required.", e.getMessage());
        }
    }
}
