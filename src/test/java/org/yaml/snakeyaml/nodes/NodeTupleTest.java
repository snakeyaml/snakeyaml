package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

public class NodeTupleTest extends TestCase {

    public void testNodeTuple1() {
        Node node = new ScalarNode("!tag", "value1", null, null, null);
        try {
            new NodeTuple(null, node);
            fail("Node must be provided.");
        } catch (Exception e) {
            assertEquals("Nodes must be provided.", e.getMessage());
        }
    }

    public void testNodeTuple2() {
        Node node = new ScalarNode("!tag", "value1", null, null, null);
        try {
            new NodeTuple(node, null);
            fail("Node must be provided.");
        } catch (Exception e) {
            assertEquals("Nodes must be provided.", e.getMessage());
        }
    }

    public void testToString() {
        Node key = new ScalarNode(Tags.STR, "key1", null, null, null);
        Node value = new ScalarNode(Tags.STR, "value1", null, null, null);
        NodeTuple tuple = new NodeTuple(key, value);
        assertEquals(
                "<NodeTuple keyNode=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=key1)>; valueNode=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=value1)>>",
                tuple.toString());
    }

}
