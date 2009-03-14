/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;

public class AliasTokenTest extends TestCase {

    public void testEquals() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        AliasToken token = new AliasToken("*id123", mark, mark);
        assertFalse(token.equals(mark));
    }

    public void testGetArguments() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        AliasToken token = new AliasToken("*id123", mark, mark);
        assertEquals("value=*id123", token.getArguments());
    }

    public void testGetTokenId() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        AliasToken token = new AliasToken("&id123", mark, mark);
        assertEquals("<alias>", token.getTokenId());
    }
}
