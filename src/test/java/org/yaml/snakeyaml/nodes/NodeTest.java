/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;

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

    public void testGetEndMark() {
        Mark mark1 = new Mark("name", 5, 2, 12, "afd asd asd", 7);
        Mark mark2 = new Mark("name", 6, 3, 13, "afd asd asd", 8);
        Node node = new ScalarNode("!foo", "bla-bla", mark1, mark2, '"');
        assertEquals(mark1, node.getStartMark());
        assertEquals(mark2, node.getEndMark());
    }

}
