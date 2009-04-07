/*
 * See LICENSE file in distribution for copyright and licensing information.
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
        System.out.println(output);
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
