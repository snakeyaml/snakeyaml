/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.composer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.resolver.Resolver;

public class ComposerImplTest extends TestCase {

    public void testGetNode() {
        String data = "american:\n  - Boston Red Sox";
        Node node = compose(data);
        assertNotNull(node);
        assertTrue(node instanceof MappingNode);
        String data2 = "---\namerican:\n- Boston Red Sox";
        Node node2 = compose(data2);
        assertNotNull(node2);
        assertFalse(node.equals(node2));
    }

    private Node compose(String data) {
        Reader reader = new Reader(data);
        Parser parser = new ParserImpl(reader);
        Resolver resolver = new Resolver();
        Composer composer = new Composer(parser, resolver);
        Node node = composer.getSingleNode();
        return node;
    }
}
