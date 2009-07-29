/**
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.resolver.Resolver;

public class LoaderTest extends TestCase {

    public void testCompose1() {
        Loader loader = new Loader();
        loader.setResolver(new Resolver());
        String yaml = "abc: 3";
        MappingNode node = (MappingNode) loader.compose(new StringReader(yaml));
        List<NodeTuple> nodes = node.getValue();
        assertEquals(1, nodes.size());
        NodeTuple pairs = nodes.get(0);
        ScalarNode key = (ScalarNode) pairs.getKeyNode();
        assertEquals(Tags.STR, key.getTag());
        assertEquals("abc", key.getValue());
        //
        ScalarNode value = (ScalarNode) pairs.getValueNode();
        assertEquals(Tags.INT, value.getTag());
        assertEquals("3", value.getValue());
        //
        assertTrue(node
                .toString()
                .startsWith(
                        "<org.yaml.snakeyaml.nodes.MappingNode (tag=tag:yaml.org,2002:map, values={ key=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=abc)>; value=Node<"));
        assertTrue(node.toString().endsWith("> })>"));
    }

    public void testCompose2() {
        Loader loader = new Loader();
        loader.setResolver(new Resolver());
        String yaml = "3";
        ScalarNode node = (ScalarNode) loader.compose(new StringReader(yaml));
        assertEquals(Tags.INT, node.getTag());
        assertEquals("3", node.getValue());
        // not sure whether it should be null or 0
        assertEquals(new Character('\u0000'), node.getStyle());
        assertEquals(Object.class, node.getType());
        Mark mark = node.getStartMark();
        assertEquals(0, mark.getColumn());
        assertEquals(0, mark.getLine());
        assertEquals("<reader>", mark.getName());
        assertNull(mark.get_snippet());
        assertEquals("<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:int, value=3)>",
                node.toString());
    }

    public void testComposeAll() {
        Loader loader = new Loader();
        loader.setResolver(new Resolver());
        String yaml = "abc: 3\n---\n2\n---\n- qwe\n- asd\n";
        int counter = 0;
        for (Node node : loader.composeAll(new StringReader(yaml))) {
            assertNotNull(node);
            switch (counter++) {
            case 0:
                assertEquals(NodeId.mapping, node.getNodeId());
                break;
            case 1:
                assertEquals(NodeId.scalar, node.getNodeId());
                break;
            case 2:
                assertEquals(NodeId.sequence, node.getNodeId());
                break;
            default:
                fail("Unexpected document.");
            }
        }
    }
}
