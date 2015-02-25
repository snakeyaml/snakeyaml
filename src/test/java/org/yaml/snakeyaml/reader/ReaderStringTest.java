/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.reader;

import java.util.regex.Matcher;

import junit.framework.TestCase;

public class ReaderStringTest extends TestCase {

    public void testCheckPrintable() {
        StreamReader reader = new StreamReader("test");
        reader.checkPrintable("test");
        Matcher matcher = StreamReader.NON_PRINTABLE.matcher("test");
        assertFalse(matcher.find());

        try {
            reader.checkPrintable("test".toCharArray(), 0, 4);
        } catch (ReaderException e) {
            fail();
        }

    }

    public void testCheckNonPrintable() {
        Matcher matcher = StreamReader.NON_PRINTABLE.matcher("test\u0005 fail");
        assertTrue(matcher.find());
        try {
            new StreamReader("test\u0005 fail");
            fail("Non printable Unicode characters must not be accepted.");
        } catch (ReaderException e) {
            assertEquals(
                    "unacceptable character '' (0x5) special characters are not allowed\nin \"'string'\", position 4",
                    e.toString());
        }
    }

    /**
     * test that regular expression and array check work the same
     */
    public void testCheckAll() {
        StreamReader streamReader = new StreamReader("");
        for (char i = 0; i < 256 * 256 - 1; i++) {
            char[] chars = new char[1];
            chars[0] = i;
            String str = new String(chars);
            Matcher matcher = StreamReader.NON_PRINTABLE.matcher(str);
            boolean regularExpressionResult = !matcher.find();

            boolean charsArrayResult = true;
            try {
                streamReader.checkPrintable(chars, 0, 1);
            } catch (Exception e) {
                String error = e.getMessage();
                assertTrue(
                        error,
                        error.startsWith("unacceptable character")
                                || error.equals("special characters are not allowed"));
                charsArrayResult = false;
            }
            assertEquals("Failed for #" + i, regularExpressionResult, charsArrayResult);
        }
    }

    public void testForward() {
        StreamReader reader = new StreamReader("test");
        while (reader.peek() != '\u0000') {
            reader.forward(1);
        }
        reader = new StreamReader("test");
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
        StreamReader reader = new StreamReader("test");
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
