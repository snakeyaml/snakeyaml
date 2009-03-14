/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

public class ScalarNodeTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testGetNodeId() {
        Node node = new ScalarNode("str", "text", null, null, '>');
        assertEquals(NodeId.scalar, node.getNodeId());
    }

    public void testToString() {
        Node node = new ScalarNode("str", "text", null, null, '>');
        assertTrue(node.toString().contains("ScalarNode"));
        assertTrue(node.toString().contains("tag="));
        assertTrue(node.toString().contains("value="));
    }

}
