/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;

public class AnchorTokenTest extends TestCase {

    public void testGetArguments() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        AnchorToken token = new AnchorToken("&id123", mark, mark);
        assertEquals("value=&id123", token.getArguments());
    }

    public void testGetTokenId() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        AnchorToken token = new AnchorToken("&id123", mark, mark);
        assertEquals("<anchor>", token.getTokenId());
    }
}
