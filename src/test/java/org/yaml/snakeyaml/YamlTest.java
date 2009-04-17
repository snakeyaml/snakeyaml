package org.yaml.snakeyaml;

import java.io.ByteArrayInputStream;
import java.io.StringReader;

import junit.framework.TestCase;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;

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

    public void testComposeFromString() {
        Yaml yaml = new Yaml();
        MappingNode node = (MappingNode) yaml.compose("abc: 56");
        assertEquals("abc", node.getValue().get(0)[0].getValue());
        assertEquals("56", node.getValue().get(0)[1].getValue());
    }

    public void testComposeFromString2() {
        try {
            Yaml yaml = new Yaml();
            yaml.compose("abc: 56\n---\n123\n---\n456");
            fail("YAML contans more then one ducument.");
        } catch (Exception e) {
            assertEquals("expected a single document in the stream; but found another document", e
                    .getMessage());
        }
    }

    public void testComposeFromReader() {
        Yaml yaml = new Yaml();
        MappingNode node = (MappingNode) yaml.compose(new StringReader("abc: 56"));
        assertEquals("abc", node.getValue().get(0)[0].getValue());
        assertEquals("56", node.getValue().get(0)[1].getValue());
    }

    public void testComposeFromInputStream() {
        Yaml yaml = new Yaml();
        MappingNode node = (MappingNode) yaml
                .compose(new ByteArrayInputStream("abc: 56".getBytes()));
        assertEquals("abc", node.getValue().get(0)[0].getValue());
        assertEquals("56", node.getValue().get(0)[1].getValue());
    }

    public void testComposeAllFromString() {
        Yaml yaml = new Yaml();
        boolean first = true;
        for (Node node : yaml.composeAll("abc: 56\n---\n123\n---\n456")) {
            if (first) {
                assertEquals(NodeId.mapping, node.getNodeId());
            } else {
                assertEquals(NodeId.scalar, node.getNodeId());
            }
            first = false;
        }
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

    public void testComposeAllFromInputStream() {
        Yaml yaml = new Yaml();
        boolean first = true;
        for (Node node : yaml.composeAll(new ByteArrayInputStream("abc: 56\n---\n123\n---\n456"
                .getBytes()))) {
            if (first) {
                assertEquals(NodeId.mapping, node.getNodeId());
            } else {
                assertEquals(NodeId.scalar, node.getNodeId());
            }
            first = false;
        }
    }
}
