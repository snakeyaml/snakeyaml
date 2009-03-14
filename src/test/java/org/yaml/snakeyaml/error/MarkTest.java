/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.error;

import junit.framework.TestCase;

public class MarkTest extends TestCase {

    public void testGet_snippet() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        assertEquals("    *The first line.\n    ^", mark.get_snippet());
        mark = new Mark("test1", 9, 0, 0, "The first*line.\nThe last line.", 9);
        assertEquals("    The first*line.\n             ^", mark.get_snippet());
    }

    public void testToString() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        String[] lines = mark.toString().split("\n");
        assertEquals(" in \"test1\", line 0, column 0:", lines[0]);
        assertEquals("*The first line.", lines[1].trim());
        assertEquals("^", lines[2].trim());
    }

}
