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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;

public class EmitterMultiLineTest extends TestCase {

    public void testWriteMultiLineLiteral() {
        String plain = "mama\nmila\nramu";
        Yaml yaml = new Yaml();
        String output = yaml.dump(plain);
        // System.out.println(output);
        assertEquals("|-\n  mama\n  mila\n  ramu\n", output);
        String parsed = (String) yaml.load(output);
        // System.out.println(parsed);
        assertEquals(plain, parsed);
    }

    public void testWriteMultiLineList() {
        String one = "first\nsecond\nthird";
        String two = "one\ntwo\nthree\n";
        byte[] binary = { 8, 14, 15, 10, 126, 32, 65, 65, 65 };
        List<Object> list = new ArrayList<Object>(2);
        list.add(one);
        list.add(two);
        list.add(binary);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(list);
        // System.out.println(output);
        String etalon = "- |-\n  first\n  second\n  third\n- |\n  one\n  two\n  three\n- !!binary |-\n  CA4PCn4gQUFB\n";
        assertEquals(etalon, output);
        @SuppressWarnings("unchecked")
        List<Object> parsed = (List<Object>) yaml.load(etalon);
        assertEquals(3, parsed.size());
        assertEquals(one, parsed.get(0));
        assertEquals(two, parsed.get(1));
        assertEquals(new String(binary), new String((byte[]) parsed.get(2)));
    }

    public void testWriteMultiLineLiteralWithClipChomping() {
        String source = "a: 1\nb: |\n  mama\n  mila\n  ramu\n";
        // System.out.println("Source:\n" + source);
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = (Map<String, Object>) yaml.load(source);
        String value = (String) parsed.get("b");
        // System.out.println(value);
        assertEquals("mama\nmila\nramu\n", value);
        String dumped = yaml.dump(parsed);
        // System.out.println(dumped);
        assertEquals("a: 1\nb: |\n  mama\n  mila\n  ramu\n", dumped);
    }

    public void testWriteMultiLineQuotedInFlowContext() {
        String source = "{a: 1, b: 'mama\n\n    mila\n\n    ramu'}\n";
        // System.out.println("Source:\n" + source);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.FLOW);
        Yaml yaml = new Yaml(options);
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = (Map<String, Object>) yaml.load(source);
        String value = (String) parsed.get("b");
        // System.out.println(value);
        assertEquals("mama\nmila\nramu", value);
        String dumped = yaml.dump(parsed);
        // System.out.println(dumped);
        assertEquals("{a: 1, b: \"mama\\nmila\\nramu\"}\n", dumped);
    }

    public void testWriteMultiLineLiteralWithStripChomping() {
        String source = "a: 1\nb: |-\n  mama\n  mila\n  ramu\n";
        // System.out.println("Source:\n" + source);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = (Map<String, Object>) yaml.load(source);
        String value = (String) parsed.get("b");
        // System.out.println(value);
        assertEquals("mama\nmila\nramu", value);
        String dumped = yaml.dump(parsed);
        // System.out.println(dumped);
        assertEquals(source, dumped);
    }
}
