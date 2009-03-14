/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;

public class TagTokenTest extends TestCase {

    public void testGetArguments() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        TagToken token = new TagToken(new String[] { "!foo", "!bar" }, mark, mark);
        assertEquals("value=[!foo, !bar]", token.getArguments());
    }

    public void testNoTag() {
        try {
            Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
            new TagToken(new String[] { "!foo" }, mark, mark);
            fail("Marks must be provided.");
        } catch (Exception e) {
            assertEquals("Two strings must be provided instead of 1", e.getMessage());
        }
    }

    public void testGetTokenId() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        TagToken token = new TagToken(new String[] { "!foo", "!bar" }, mark, mark);
        assertEquals("<tag>", token.getTokenId());
    }
}
