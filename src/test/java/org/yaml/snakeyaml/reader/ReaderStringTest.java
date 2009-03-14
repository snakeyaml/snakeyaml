/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.reader;

import java.util.regex.Matcher;

import junit.framework.TestCase;

public class ReaderStringTest extends TestCase {

    public void testCheckPrintable() {
        Reader reader = new Reader("test");
        reader.checkPrintable("test");
        Matcher matcher = Reader.NON_PRINTABLE.matcher("test");
        assertFalse(matcher.find());
    }

    public void testCheckNonPrintable() {
        Matcher matcher = Reader.NON_PRINTABLE.matcher("test\u0005 fail");
        assertTrue(matcher.find());
        try {
            new Reader("test\u0005 fail");
            fail("Non printable Unicode characters must not be accepted.");
        } catch (ReaderException e) {
            assertEquals(
                    "unacceptable character #5  special characters are not allowed\nin \"<string>\", position 4",
                    e.toString());
        }
    }

    public void testForward() {
        Reader reader = new Reader("test");
        while (reader.peek() != '\u0000') {
            reader.forward(1);
        }
        reader = new Reader("test");
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
    }

    public void testPeekInt() {
        Reader reader = new Reader("test");
        assertEquals('t', reader.peek(0));
        assertEquals('e', reader.peek(1));
        assertEquals('s', reader.peek(2));
        assertEquals('t', reader.peek(3));
        reader.forward(1);
        assertEquals('e', reader.peek(0));
        assertEquals('s', reader.peek(1));
        assertEquals('t', reader.peek(2));
    }

}
