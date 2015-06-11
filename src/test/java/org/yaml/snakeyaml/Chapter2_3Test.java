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

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.ScalarStyle;

/**
 * Test Chapter 2.3 from the YAML specification
 * 
 * @see http://yaml.org/spec/1.1/
 */
public class Chapter2_3Test extends TestCase {

    public void testExample_2_13() {
        YamlDocument document = new YamlDocument("example2_13.yaml");
        String data = (String) document.getNativeData();
        assertEquals("\\//||\\/||\n// ||  ||__\n", data);
    }

    public void testExample_2_14() {
        YamlDocument document = new YamlDocument("example2_14.yaml");
        String data = (String) document.getNativeData();
        assertEquals("Mark McGwire's year was crippled by a knee injury.", data);
    }

    public void testExample_2_15() {
        String etalon = "Sammy Sosa completed another fine season with great stats.\n\n  63 Home Runs\n  0.288 Batting Average\n\nWhat a year!\n";
        InputStream input = YamlDocument.class.getClassLoader().getResourceAsStream(
                YamlDocument.ROOT + "example2_15.yaml");
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.FOLDED);
        Yaml yaml = new Yaml(options);
        String data = (String) yaml.load(input);
        assertEquals(etalon, data);
        //
        String dumped = yaml.dump(data);
        String etalonDumped = Util.getLocalResource("specification/example2_15_dumped.yaml");
        assertEquals(etalonDumped, dumped);
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_16() {
        YamlDocument document = new YamlDocument("example2_16.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals(map.toString(), 3, map.size());
        assertEquals("Mark McGwire", map.get("name"));
        assertEquals("Mark set a major league home run record in 1998.\n",
                map.get("accomplishment"));
        assertEquals("65 Home Runs\n0.278 Batting Average\n", map.get("stats"));

    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17() {
        YamlDocument document = new YamlDocument("example2_17.yaml", false);
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals(map.toString(), 6, map.size());
        assertEquals("Sosa did fine.\u263A", map.get("unicode"));
        assertEquals("\b1998\t1999\t2000\n", map.get("control"));
        assertEquals("\r\n is \r\n", map.get("hexesc"));
        assertEquals("\"Howdy!\" he cried.", map.get("single"));
        assertEquals(" # not a 'comment'.", map.get("quoted"));
        assertEquals("|\\-*-/|", map.get("tie-fighter"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17_unicode() {
        YamlDocument document = new YamlDocument("example2_17_unicode.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals("Sosa did fine.\u263A", map.get("unicode"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17_control() {
        YamlDocument document = new YamlDocument("example2_17_control.yaml", false);
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals("\b1998\t1999\t2000\n", map.get("control"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17_hexesc() {
        YamlDocument document = new YamlDocument("example2_17_hexesc.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals("\r\n is \r\n", map.get("hexesc"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17_single() {
        YamlDocument document = new YamlDocument("example2_17_single.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals("\"Howdy!\" he cried.", map.get("single"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17_quoted() {
        YamlDocument document = new YamlDocument("example2_17_quoted.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals(" # not a 'comment'.", map.get("quoted"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_17_tie_fighter() {
        YamlDocument document = new YamlDocument("example2_17_tie_fighter.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals("|\\-*-/|", map.get("tie-fighter"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_18() {
        YamlDocument document = new YamlDocument("example2_18.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals(map.toString(), 2, map.size());
        assertEquals("This unquoted scalar spans many lines.", map.get("plain"));
        assertEquals("So does this quoted scalar.\n", map.get("quoted"));
    }
}
