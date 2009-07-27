/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.StringReader;

import junit.framework.TestCase;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class YamlComposeTest extends TestCase {

    public void testComposeManyDocuments() {
        try {
            Yaml yaml = new Yaml();
            yaml.compose(new StringReader("abc: 56\n---\n123\n---\n456"));
            fail("YAML contans more then one document.");
        } catch (Exception e) {
            assertEquals("expected a single document in the stream; but found another document", e
                    .getMessage());
        }
    }

    public void testComposeFromReader() {
        Yaml yaml = new Yaml();
        MappingNode node = (MappingNode) yaml.compose(new StringReader("abc: 56"));
        ScalarNode node1 = (ScalarNode) node.getValue().get(0).getKeyNode();
        assertEquals("abc", node1.getValue());
        ScalarNode node2 = (ScalarNode) node.getValue().get(0).getValueNode();
        assertEquals("56", node2.getValue());
    }

    public void testComposeAllFromReader() {
        Yaml yaml = new Yaml();
        boolean first = true;
        for (Node node : yaml.composeAll(new StringReader("abc: 56\n---\n123\n---\n456"))) {
            if (first) {
                assertEquals(NodeId.mapping, node.getNodeId());
            } else {
                assertEquals(NodeId.scalar, node.getNodeId());
            }
            first = false;
        }
    }

    public void testComposeAllOneDocument() {
        Yaml yaml = new Yaml();
        for (Node node : yaml.composeAll(new StringReader("6"))) {
            assertEquals(NodeId.scalar, node.getNodeId());
        }
    }
}
