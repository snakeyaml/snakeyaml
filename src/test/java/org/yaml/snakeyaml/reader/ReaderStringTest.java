/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.reader;

import junit.framework.TestCase;

import java.io.StringReader;

public class ReaderStringTest extends TestCase {

  public void testCheckPrintable() {
    StreamReader reader = new StreamReader("test");
    assertEquals('\0', reader.peek(4));
    assertTrue(StreamReader.isPrintable("test"));
  }

  public void testCheckNonPrintable() {
    assertFalse(StreamReader.isPrintable("test\u0005 fail"));
    try {
      StreamReader reader = new StreamReader("test\u0005 fail");
      while (reader.peek() != '\0') {
        reader.forward();
      }
      fail("Non printable Unicode code points must not be accepted.");
    } catch (ReaderException e) {
      assertEquals(
          "unacceptable code point '' (0x5) special characters are not allowed\nin \"'string'\", position 4",
          e.toString());
    }
  }

  /**
   * test reading all the chars
   */
  public void testCheckAll() {
    int counterSurrogates = 0;
    for (char i = 0; i < 256 * 256 - 1; i++) {
      if (Character.isHighSurrogate(i)) {
        counterSurrogates++;
      } else {
        char[] chars = new char[1];
        chars[0] = i;
        String str = new String(chars);
        boolean regularExpressionResult = StreamReader.isPrintable(str);

        boolean charsArrayResult = true;
        try {
          new StreamReader(new StringReader(str)).peek();
        } catch (Exception e) {
          String error = e.getMessage();
          assertTrue(error, error.startsWith("unacceptable character")
              || error.equals("special characters are not allowed"));
          charsArrayResult = false;
        }
        assertEquals("Failed for #" + i, regularExpressionResult, charsArrayResult);
      }
    }
    // https://en.wikipedia.org/wiki/Universal_Character_Set_characters
    assertEquals("There are 1024 high surrogates (D800–DBFF)", 1024, counterSurrogates);
  }

  public void testHighSurrogateAlone() {
    StreamReader reader = new StreamReader("test\uD800");
    try {
      while (reader.peek() > 0) {
        reader.forward(1);
      }
    } catch (ReaderException e) {
      assertTrue(e.toString()
          .contains("(0xD800) The last char is HighSurrogate (no LowSurrogate detected)"));
      assertEquals(5, e.getPosition());
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
