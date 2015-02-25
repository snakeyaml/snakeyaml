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
package org.yaml.snakeyaml.types;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

/**
 * @see http://yaml.org/type/str.html
 */
public class StrTagTest extends AbstractTest {
    private String getData(String data, String key) {
        return (String) getMapValue(data, key);
    }

    public void testString() {
        assertEquals("abcd", getData("string: abcd", "string"));
    }

    public void testStringShorthand() {
        assertEquals("abcd", getData("string: !!str abcd", "string"));
    }

    public void testStringTag() {
        assertEquals("abcd", getData("string: !<tag:yaml.org,2002:str> abcd", "string"));
    }

    public void testUnicode() {
        // escaped 8-bit unicode character (u-umlaut):
        assertEquals("\u00fc", load("\"\\xfc\""));
        assertEquals("\\xfc", load("\\xfc"));

        // 2 escaped 8-bit unicode characters (u-umlaut following by e-grave):
        assertEquals("\u00fc\u00e8", load("\"\\xfc\\xe8\""));

        // escaped 16-bit unicode character (em dash):
        assertEquals("\u2014", load("\"\\u2014\""));

        // UTF-32 encoding is explicitly not supported
        assertEquals("\\U2014AAAA", load("'\\U2014AAAA'"));

        // (and I don't have a surrogate pair handy at the moment)
        // raw unicode characters in the stream (em dash)
        assertEquals("\u2014", load("\u2014"));
    }

    /**
     * @see http://code.google.com/p/jvyamlb/issues/detail?id=6
     */
    @SuppressWarnings("unchecked")
    public void testIssueId6() {
        Map<String, String> map = (Map<String, String>) load("---\ntest: |-\n \"Test\r\r (* error here)\"");
        assertEquals("\"Test\n\n(* error here)\"", map.get("test"));
    }

    public void testStrDump() {
        assertEquals("abc\n", dump("abc"));
    }

    public void testUnicodeDump() {
        assertEquals("\\u263a\n", dump("\\u263a"));
        assertEquals("The leading zero must be preserved.", "\\u063a\n", dump("\\u063a"));
    }

    public void testStringIntOut() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", "1");
        String output = dump(map);
        assertTrue(output, output.contains("number: '1'"));
    }

    public void testStringFloatOut() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", "1.1");
        String output = dump(map);
        assertTrue(output, output.contains("number: '1.1'"));
    }

    public void testStringBoolOut() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("number", "True");
        String output = dump(map);
        assertTrue(output, output.contains("number: 'True'"));
    }

    public void testEmitLongString() {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        String output = dump(str);
        assertEquals(str + "\n", output);
    }

    public void testEmitLongStringWithCR() {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\n\n";
        String output = dump(str);
        assertEquals("|+\n  " + str, output);
    }

    public void testEmitDoubleQuoted() {
        String str = "\"xx\"";
        String output = dump(str);
        assertEquals("'" + str + "'\n", output);
    }

    /**
     * http://pyyaml.org/ticket/196
     */
    public void testEmitQuoted() {
        List<String> list = new ArrayList<String>(3);
        list.add("This is an 'example'.");
        list.add("This is an \"example\".");
        list.add("123");
        String output = dump(list);
        assertEquals("[This is an 'example'., This is an \"example\"., '123']\n", output);
        // single quoted
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.SINGLE_QUOTED);
        Yaml yaml = new Yaml(options);
        String output2 = yaml.dump(list);
        // System.out.println(output2);
        assertEquals("- 'This is an ''example''.'\n- 'This is an \"example\".'\n- '123'\n", output2);
        // double quoted
        DumperOptions options2 = new DumperOptions();
        options2.setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED);
        yaml = new Yaml(options2);
        String output3 = yaml.dump(list);
        // System.out.println(output2);
        assertEquals("- \"This is an 'example'.\"\n- \"This is an \\\"example\\\".\"\n- \"123\"\n",
                output3);
    }

    public void testEmitEndOfLine() {
        String str = "xxxxxxx\n";
        String output = dump(str);
        assertEquals("|\n  " + str, output);
    }

    public void testDumpUtf16() throws UnsupportedEncodingException {
        String str = "xxx";
        assertEquals(3, str.toString().length());
        Yaml yaml = new Yaml();
        Charset charset = Charset.forName("UTF-16");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(stream, charset);
        yaml.dump(str, writer);
        assertEquals(str + "\n", stream.toString("UTF-16"));
        assertEquals("Must include BOM: " + stream.toString(), (1 + 3 + 1) * 2, stream.toString()
                .length());
    }
}
