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
package org.yaml.snakeyaml.issues.issue148;

import java.util.Formatter;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.ReaderException;

public class PrintableUnicodeTest extends TestCase {
    public void testFFFD() {
        Yaml yaml = createYaml();
        String fffd = yaml.dump("\uFFFD");
        assertEquals("\"\\ufffd\"\n", fffd);
    }

    public void testSerialization() {
        // test serialization of all Unicode codepoints
        Yaml yaml = createYaml();
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
            String original = Character.toString((char) c);
            String serialized = yaml.dump(original);

            // "On output, a YAML processor must only produce these acceptable
            // characters,
            // and should also escape all non-printable Unicode characters."
            for (int i = 0; i < serialized.length(); i++) {
                int cp = (int) serialized.charAt(i);
                if (!isAcceptable(cp))
                    fail(String.format(
                            "U+%04x: Serialization produced result with unacceptable U+%04x\n", c,
                            cp));
                if (!isPrintable(cp))
                    fail(String.format(
                            "U+%04x: Serialization produced result with nonprintable U+%04x\n", c,
                            cp));
            }
        }
    }

    public void testDeserialization() {
        // test deserialization of non-escaped codepoints
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
            // ignore breaks, which have special meaning
            if (c == 0x0A || c == 0x0D || c == 0x85 || c == 0x2028 || c == 0x2029)
                continue;
            if (!isAcceptable(c) || c == 0x27)
                continue;
            String expected = Character.toString((char) c);
            String serialized = "'" + expected + "'";

            String result;
            try {
                result = new Yaml().load(serialized).toString();
            } catch (ReaderException e) {
                fail(String
                        .format("U+%04x: Deserialization threw ReaderException for an acceptable character\n",
                                c));
                continue;
            }
            if (!result.equals(expected))
                fail(String.format("U+%04x: Deserialization incorrect: %s\n", c, hexdump(result)));
        }
    }

    public void testDeserialization2() {
        // test deserialization of escaped codepoints
        // "Any such characters must be presented using escape sequences."
        for (int c = Character.MIN_VALUE; c <= Character.MAX_VALUE; c++) {
            String expected = Character.toString((char) c);
            String serialized = String.format("\"\\u%04x\"", c);

            String result;
            try {
                result = new Yaml().load(serialized).toString();
            } catch (ReaderException e) {
                fail(String
                        .format("U+%04x: Deserialization threw ReaderException for an acceptable escaped character\n",
                                c));
                continue;
            }
            if (!result.equals(expected))
                fail(String.format("U+%04x: Deserialization of escaped character incorrect: %s\n",
                        c, hexdump(result)));
        }
    }

    private Yaml createYaml() {
        DumperOptions options = new DumperOptions();
        options.setAllowUnicode(false);
        options.setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED);
        return new Yaml(options);
    }

    /**
     * Test whether a character is printable, according to the YAML spec.
     * ('c-printable')
     */
    public static boolean isPrintable(int c) {
        return c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0x7E) // 8
                                                                            // bit
                || c == 0x85 || (c >= 0xA0 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD) // 16
                                                                                           // bit
                || (c >= 0x10000 && c <= 0x10FFFF); // 32 bit
    }

    /**
     * "On input, a YAML processor must accept all printable ASCII characters,
     * the space, tab, line break, and all Unicode characters beyond #x9F. On
     * output, a YAML processor must only produce these acceptable characters,
     * and should also escape all non-printable Unicode characters. The allowed
     * character range explicitly excludes the surrogate block #xD800-#xDFFF,
     * DEL #x7F, the C0 control block #x0-#x1F (except for #x9, #xA, and #xD),
     * the C1 control block #x80-#x9F, #xFFFE, and #xFFFF."
     */
    public static boolean isAcceptable(int c) {
        return (c >= 0x20 && c <= 0x7e // accept all printable ASCII characters,
                                       // the space,
                || c == 0x09 // tab,
                || c == 0x0A || c == 0x0D || c == 0x85 || c == 0x2028 || c == 0x2029 // line
                                                                                     // break,
        || isUnicodeCharacter(c) && c >= 0x9F // and all Unicode characters
                                              // beyond #x9F
        ) && !( // The allowed character range explicitly excludes
                c >= 0xD800 && c <= 0xDFFF // the surrogate block #xD800-#xDFFF
                        || c == 0x7f // DEL #x7F,
                        || c <= 0x1F && !(c == 0x09 || c == 0x0A || c == 0x0D) // the
                                                                               // C0
                                                                               // control
                                                                               // block
                                                                               // #x0-#x1F
                                                                               // (except
                                                                               // for
                                                                               // #x9,
                                                                               // #xA,
                                                                               // and
                                                                               // #xD),
                        || c >= 0x80 && c <= 0x9F // the C1 control block
                                                  // #x80-#x9F,
                        || c == 0xFFFE // #xFFFE,
                || c == 0xFFFF // and #xFFFF.
                );
    }

    /**
     * Tests whether a codepoint is a designated Unicode noncharacter or not.
     */
    public static boolean isUnicodeCharacter(int c) {
        int plane = c / 0x10000;
        return !(c >= 0xFDD0 && c <= 0xFDEF) && (plane <= 16 && (c & 0xFFFE) != 0xFFFE);
    }

    public static String hexdump(String input) {
        StringBuilder result = new StringBuilder();
        Formatter formatter = new Formatter(result);
        for (int i = 0; i < input.length(); i++)
            formatter.format("%02x ", (int) input.charAt(i));
        return result.toString();
    }
}