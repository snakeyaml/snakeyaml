/**
 * Copyright (c) 2008-2010, http://www.snakeyaml.org
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

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;

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
        String etalon = "{aaa: '0123456789 0123456789\n\n    0123456789 0123456789', bbb: '\n\n    bla-bla'}\n";
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
        String etalon = "{\n  aaa: '0123456789 0123456789\n\n    0123456789 0123456789',\n  bbb: '\n\n    bla-bla'\n}\n";
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
}
