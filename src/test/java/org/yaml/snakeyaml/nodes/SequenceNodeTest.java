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
}
