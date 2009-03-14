/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;

public class DirectiveTokenTest extends TestCase {

    public void testGetArguments() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        DirectiveToken token = new DirectiveToken("YAML", null, mark, mark);
        assertEquals("name=YAML", token.getArguments());
    }

    public void testInvalidList() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        List<Integer> list = new LinkedList<Integer>();
        list.add(new Integer(1));
        try {
            new DirectiveToken("YAML", list, mark, mark);
            fail("List must have 2 values.");
        } catch (Exception e) {
            assertEquals("Two strings must be provided instead of 1", e.getMessage());
        }
    }

    public void testTag() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        List<String> list = new LinkedList<String>();
        list.add("!foo");
        list.add("!bar");
        DirectiveToken token = new DirectiveToken("TAG", list, mark, mark);
        assertEquals("name=TAG, value=[!foo, !bar]", token.getArguments());
    }

    public void testList() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        List<Integer> list = new LinkedList<Integer>();
        list.add(new Integer(1));
        list.add(new Integer(1));
        DirectiveToken token = new DirectiveToken("YAML", list, mark, mark);
        assertEquals("name=YAML, value=[1, 1]", token.getArguments());
    }

    public void testGetTokenId() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        DirectiveToken token = new DirectiveToken("YAML", null, mark, mark);
        assertEquals("<directive>", token.getTokenId());
    }
}
