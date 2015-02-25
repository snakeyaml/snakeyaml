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
package org.yaml.snakeyaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class DumperOptionsTest extends TestCase {

    public void testDefaultStyle() {
        DumperOptions options = new DumperOptions();
        Yaml yaml = new Yaml(options);
        assertEquals("abc\n", yaml.dump("abc"));
        // string which looks like integer
        assertEquals("'123'\n", yaml.dump("123"));
        //
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        yaml = new Yaml(options);
        assertEquals("\"123\"\n", yaml.dump("123"));
        //
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        yaml = new Yaml(options);
        assertEquals("'123'\n", yaml.dump("123"));
        //
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        yaml = new Yaml(options);
        assertEquals("'123'\n", yaml.dump("123"));
        assertEquals("abc\n", yaml.dump("abc"));
        // null check
        try {
            options.setDefaultScalarStyle(null);
            fail("Null must not be accepted.");
        } catch (NullPointerException e) {
            assertEquals("Use ScalarStyle enum.", e.getMessage());
        }
    }

    public void testDefaultFlowStyle() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals("[1, 2, 3]\n", yaml.dump(list));
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        yaml = new Yaml(options);
        assertEquals("[1, 2, 3]\n", yaml.dump(list));
        //
        options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setPrettyFlow(true);
        yaml = new Yaml(options);
        assertEquals("[\n  1,\n  2,\n  3]\n", yaml.dump(list));
        //
        options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
        assertEquals("- 1\n- 2\n- 3\n", yaml.dump(list));
        // null check
        try {
            options.setDefaultFlowStyle(null);
            fail("Null must not be accepted.");
        } catch (NullPointerException e) {
            assertEquals("Use FlowStyle enum.", e.getMessage());
        }
    }

    public void testDefaultFlowStyleNested() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("a", "b");
        map.put("c", list);
        String result = yaml.dump(map);
        assertEquals("a: b\nc: [1, 2, 3]\n", result);
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        yaml = new Yaml(options);
        assertEquals("{a: b, c: [1, 2, 3]}\n", yaml.dump(map));
        //
        options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setPrettyFlow(true);
        yaml = new Yaml(options);
        result = yaml.dump(map);
        assertEquals("{\n  a: b,\n  c: [\n    1,\n    2,\n    3]\n  \n}\n", result);
        //
        options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        yaml = new Yaml(options);
        assertEquals("a: b\nc:\n- 1\n- 2\n- 3\n", yaml.dump(map));
    }

    public void testCanonical() {
        Yaml yaml = new Yaml();
        assertEquals("123\n", yaml.dump(123));
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        options.setCanonical(true);
        yaml = new Yaml(options);
        assertEquals("---\n!!int \"123\"\n", yaml.dump(123));
        //
        options = new DumperOptions();
        options.setCanonical(false);
        yaml = new Yaml(options);
        assertEquals("123\n", yaml.dump(123));
    }

    public void testIndent() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        DumperOptions options = new DumperOptions();
        options.setCanonical(true);
        yaml = new Yaml(options);
        assertEquals("---\n!!seq [\n  !!int \"1\",\n  !!int \"2\",\n]\n", yaml.dump(list));
        //
        options.setIndent(4);
        yaml = new Yaml(options);
        assertEquals("---\n!!seq [\n    !!int \"1\",\n    !!int \"2\",\n]\n", yaml.dump(list));
        //
        try {
            options.setIndent(0);
            fail();
        } catch (YAMLException e) {
            assertTrue(true);
        }
        try {
            options.setIndent(-2);
            fail();
        } catch (YAMLException e) {
            assertTrue(true);
        }
        try {
            options.setIndent(11);
            fail();
        } catch (YAMLException e) {
            assertTrue(true);
        }
        //
        assertTrue(Emitter.MIN_INDENT > 0);
        assertTrue(Emitter.MIN_INDENT < Emitter.MAX_INDENT);
        assertTrue(Emitter.MAX_INDENT < 20);
    }

    public void testLineBreak() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        DumperOptions options = new DumperOptions();
        options.setCanonical(true);
        yaml = new Yaml(options);
        assertEquals("---\n!!seq [\n  !!int \"1\",\n  !!int \"2\",\n]\n", yaml.dump(list));
        //
        options.setLineBreak(DumperOptions.LineBreak.WIN);
        yaml = new Yaml(options);
        String output = yaml.dump(list);
        assertEquals("---\r\n!!seq [\r\n  !!int \"1\",\r\n  !!int \"2\",\r\n]\r\n", output);
        // null check
        try {
            options.setLineBreak(null);
            fail("Null must not be accepted.");
        } catch (NullPointerException e) {
            assertEquals("Specify line break.", e.getMessage());
        }
    }

    public void testLineBreakForPlatform() {
        DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.getPlatformLineBreak();
        assertEquals("Line break must match platform's default.",
                System.getProperty("line.separator"), lineBreak.getString());
        //
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        DumperOptions options = new DumperOptions();
        options.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
        yaml = new Yaml(options);
        assertEquals("[1, 2]", yaml.dump(list).trim());
    }

    public void testLineBreakForPlatformUnix() {
        System.setProperty("line.separator", "\n");
        assertEquals("\n", System.getProperty("line.separator"));
        DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.getPlatformLineBreak();
        assertEquals("Line break must match platform's default.",
                System.getProperty("line.separator"), lineBreak.getString());
        assertEquals("Unknown Line break must match UNIX line break.", "\n", lineBreak.getString());
    }

    public void testLineBreakForPlatformMac() {
        System.setProperty("line.separator", "\r");
        assertEquals("\r", System.getProperty("line.separator"));
        DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.getPlatformLineBreak();
        assertEquals("Line break must match platform's default.",
                System.getProperty("line.separator"), lineBreak.getString());
        assertEquals("Unknown Line break must match UNIX line break.", "\r", lineBreak.getString());
    }

    public void testLineBreakForPlatformWin() {
        System.setProperty("line.separator", "\r\n");
        assertEquals("\r\n", System.getProperty("line.separator"));
        DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.getPlatformLineBreak();
        assertEquals("Line break must match platform's default.",
                System.getProperty("line.separator"), lineBreak.getString());
        assertEquals("Unknown Line break must match UNIX line break.", "\r\n",
                lineBreak.getString());
    }

    public void testLineBreakForPlatformUnknown() {
        System.setProperty("line.separator", "\n\r");
        assertEquals("\n\r", System.getProperty("line.separator"));
        DumperOptions.LineBreak lineBreak = DumperOptions.LineBreak.getPlatformLineBreak();
        assertEquals("Unknown Line break must match UNIX line break.", "\n", lineBreak.getString());
    }

    public void testExplicitStart() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals("[1, 2, 3]\n", yaml.dump(list));
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        options.setExplicitStart(true);
        yaml = new Yaml(options);
        assertEquals("--- [1, 2, 3]\n", yaml.dump(list));
        //
        options.setExplicitEnd(true);
        yaml = new Yaml(options);
        assertEquals("--- [1, 2, 3]\n...\n", yaml.dump(list));
    }

    public void testVersion() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals("[1, 2, 3]\n", yaml.dump(list));
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        options.setVersion(DumperOptions.Version.V1_1);
        yaml = new Yaml(options);
        assertEquals("%YAML 1.1\n--- [1, 2, 3]\n", yaml.dump(list));
        //
        options.setVersion(DumperOptions.Version.V1_0);
        yaml = new Yaml(options);
        assertEquals("%YAML 1.0\n--- [1, 2, 3]\n", yaml.dump(list));
        //
        assertEquals("Version: 1.1", DumperOptions.Version.V1_1.toString());
    }

    public void testTags() {
        Yaml yaml = new Yaml();
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals("[1, 2, 3]\n", yaml.dump(list));
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        Map<String, String> tags = new LinkedHashMap<String, String>();
        tags.put("!foo!", "bar");
        options.setTags(tags);
        yaml = new Yaml(options);
        assertEquals("%TAG !foo! bar\n--- [1, 2, 3]\n", yaml.dump(list));
        //
        options = new DumperOptions();
        tags.put("!yaml!", Tag.PREFIX);
        yaml = new Yaml(options);
        assertEquals("foo\n", yaml.dump("foo"));
    }

    public void testAllowUnicode() {
        Yaml yaml = new Yaml();
        assertEquals("out: " + yaml.dump("\u00DCber"), "\u00DCber\n", yaml.dump("\u00DCber"));
        //
        DumperOptions options = new DumperOptions();
        options = new DumperOptions();
        options.setAllowUnicode(false);
        yaml = new Yaml(options);
        assertEquals("\"\\xdcber\"\n", yaml.dump("\u00DCber"));
    }

    public void testToString() {
        DumperOptions.ScalarStyle scalarStyle = DumperOptions.ScalarStyle.LITERAL;
        assertEquals("Scalar style: '|'", scalarStyle.toString());
        //
        DumperOptions.FlowStyle flowStyle = DumperOptions.FlowStyle.BLOCK;
        assertEquals("Flow style: 'false'", flowStyle.toString());
        //
        DumperOptions.LineBreak lb = DumperOptions.LineBreak.UNIX;
        assertEquals("Line break: UNIX", lb.toString());
    }

    public void testWithRepresenter() {
        Representer representer = new Representer();
        DumperOptions options = new DumperOptions();
        options.setIndent(4);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(representer, options);
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("a", "b");
        map.put("c", list);
        assertEquals("a: b\nc:\n- 1\n- 2\n- 3\n", yaml.dump(map));
    }

    public void testSplitLinesDoubleQuoted() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        Yaml yaml;
        String output;

        // Split lines enabled (default)
        assertTrue(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals("\"1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888\\\n  \\ 9999999999 0000000000\"\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals("\"1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000\"\n", output);
    }

    public void testSplitLinesSingleQuoted() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        Yaml yaml;
        String output;

        // Split lines enabled (default)
        assertTrue(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals("'1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888\n  9999999999 0000000000'\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals("'1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000'\n", output);
    }

    public void testSplitLinesFolded() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.FOLDED);
        Yaml yaml;
        String output;

        // Split lines enabled (default)
        assertTrue(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals(">-\n  1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888\n  9999999999 0000000000\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals(">-\n  1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000\n", output);
    }

    public void testSplitLinesLiteral() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.LITERAL);
        Yaml yaml;
        String output;

        // Split lines enabled (default) -- split lines does not apply to literal style
        assertTrue(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals("|-\n  1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000\n", output);
    }

    public void testSplitLinesPlain() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        Yaml yaml;
        String output;

        // Split lines enabled (default) -- split lines does not apply to plain style
        assertTrue(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        assertEquals("1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000\n", output);
    }
}
