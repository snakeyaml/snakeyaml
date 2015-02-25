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
package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;

public class EmitterTest extends TestCase {

    public void testWriteFolded() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.FOLDED);
        String folded = "0123456789 0123456789\n0123456789 0123456789";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("aaa", folded);
        map.put("bbb", "\nbla-bla\n");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        String etalon = "\"aaa\": >-\n  0123456789 0123456789\n\n  0123456789 0123456789\n\"bbb\": >2\n\n  bla-bla\n";
        assertEquals(etalon, output);
    }

    public void testWriteLiteral() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.LITERAL);
        String folded = "0123456789 0123456789 0123456789 0123456789";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("aaa", folded);
        map.put("bbb", "\nbla-bla\n");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        String etalon = "\"aaa\": |-\n  0123456789 0123456789 0123456789 0123456789\n\"bbb\": |2\n\n  bla-bla\n";
        assertEquals(etalon, output);
    }

    public void testWritePlain() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.PLAIN);
        String folded = "0123456789 0123456789\n0123456789 0123456789";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("aaa", folded);
        map.put("bbb", "\nbla-bla");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        String etalon = "aaa: |-\n  0123456789 0123456789\n  0123456789 0123456789\nbbb: |2-\n\n  bla-bla\n";
        assertEquals(etalon, output);
    }

    public void testWritePlainPretty() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.PLAIN);
        options.setPrettyFlow(true);

        String folded = "0123456789 0123456789\n0123456789 0123456789";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("aaa", folded);
        map.put("bbb", "\nbla-bla");

        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        String etalon = "aaa: |-\n  0123456789 0123456789\n  0123456789 0123456789\nbbb: |2-\n\n  bla-bla\n";
        assertEquals(etalon, output);
    }

    public void testWriteSingleQuoted() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.SINGLE_QUOTED);
        String folded = "0123456789 0123456789\n0123456789 0123456789";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("aaa", folded);
        map.put("bbb", "\nbla-bla");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        String etalon = "'aaa': '0123456789 0123456789\n\n  0123456789 0123456789'\n'bbb': '\n\n  bla-bla'\n";
        assertEquals(etalon, output);
    }

    public void testWriteDoubleQuoted() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED);
        String folded = "0123456789 0123456789\n0123456789 0123456789";
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("aaa", folded);
        map.put("bbb", "\nbla-bla");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        String etalon = "\"aaa\": \"0123456789 0123456789\\n0123456789 0123456789\"\n\"bbb\": \"\\nbla-bla\"\n";
        assertEquals(etalon, output);
    }

    // Issue #158
    public void testWriteSupplementaryUnicode() throws IOException {
        DumperOptions options = new DumperOptions();
        String burger = new String(Character.toChars(0x1f354));
        String halfBurger = "\uD83C";
        StringWriter output = new StringWriter();
        Emitter emitter = new Emitter(output, options);

        emitter.emit(new StreamStartEvent(null, null));
        emitter.emit(new DocumentStartEvent(null, null, false, null, null));
        emitter.emit(new ScalarEvent(null, null, new ImplicitTuple(true, false), burger
                + halfBurger, null, null, '"'));
        String expected = "! \"\\U0001f354\\ud83c\"";
        assertEquals(expected, output.toString());
    }

    public void testSplitLineExpectFirstFlowSequenceItem() {

        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setWidth(8);
        Yaml yaml;
        String output;
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("12345", Arrays.asList("1111111111"));

        // Split lines enabled (default)
        yaml = new Yaml(options);
        output = yaml.dump(map);
        assertEquals("{\"12345\": [\n    \"1111111111\"]}\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump(map);
        assertEquals("{\"12345\": [\"1111111111\"]}\n", output);
    }

    public void testSplitLineExpectFlowSequenceItem() {

        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setWidth(8);
        Yaml yaml;
        String output;

        // Split lines enabled (default)
        yaml = new Yaml(options);
        output = yaml.dump(Arrays.asList("1111111111", "2222222222"));
        assertEquals("[\"1111111111\",\n  \"2222222222\"]\n", output);
        output = yaml.dump(Arrays.asList("1", "2"));
        assertEquals("[\"1\", \"2\"]\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump(Arrays.asList("1111111111", "2222222222"));
        assertEquals("[\"1111111111\", \"2222222222\"]\n", output);
        output = yaml.dump(Arrays.asList("1", "2"));
        assertEquals("[\"1\", \"2\"]\n", output);
    }

    public void testSplitLineExpectFirstFlowMappingKey() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setWidth(16);
        Yaml yaml;
        String output;
        Map<String, String> nonSplitMap = new TreeMap<String, String>();
        nonSplitMap.put("3", "4");
        Map<String, Map<String, String>> nonSplitContainerMap = new TreeMap<String, Map<String, String>>();
        nonSplitContainerMap.put("1 2", nonSplitMap);
        Map<String, String> splitMap = new TreeMap<String, String>();
        splitMap.put("3333333333", "4444444444");
        Map<String, Map<String, String>> splitContainerMap = new TreeMap<String, Map<String, String>>();
        splitContainerMap.put("1111111111 2222222222", splitMap);

        // Split lines enabled (default)
        yaml = new Yaml(options);
        output = yaml.dump(splitContainerMap);
        assertEquals("{\"1111111111 2222222222\": {\n    \"3333333333\": \"4444444444\"}}\n", output);
        output = yaml.dump(nonSplitContainerMap);
        assertEquals("{\"1 2\": {\"3\": \"4\"}}\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump(splitContainerMap);
        assertEquals("{\"1111111111 2222222222\": {\"3333333333\": \"4444444444\"}}\n", output);
        output = yaml.dump(nonSplitContainerMap);
        assertEquals("{\"1 2\": {\"3\": \"4\"}}\n", output);
    }

    public void testSplitLineExpectFlowMappingKey() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setWidth(16);
        Yaml yaml;
        String output;
        Map<String, String> nonSplitMap = new TreeMap<String, String>();
        nonSplitMap.put("1", "2");
        nonSplitMap.put("3", "4");
        Map<String, String> splitMap = new TreeMap<String, String>();
        splitMap.put("1111111111", "2222222222");
        splitMap.put("3333333333", "4444444444");

        // Split lines enabled (default)
        yaml = new Yaml(options);
        output = yaml.dump(splitMap);
        assertEquals("{\"1111111111\": \"2222222222\",\n  \"3333333333\": \"4444444444\"}\n", output);
        output = yaml.dump(nonSplitMap);
        assertEquals("{\"1\": \"2\", \"3\": \"4\"}\n", output);

        // Split lines disabled
        options.setSplitLines(false);
        assertFalse(options.getSplitLines());
        yaml = new Yaml(options);
        output = yaml.dump(splitMap);
        assertEquals("{\"1111111111\": \"2222222222\", \"3333333333\": \"4444444444\"}\n", output);
        output = yaml.dump(nonSplitMap);
        assertEquals("{\"1\": \"2\", \"3\": \"4\"}\n", output);
    }
}
