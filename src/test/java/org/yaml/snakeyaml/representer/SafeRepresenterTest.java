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
package org.yaml.snakeyaml.representer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.StreamReader;

public class SafeRepresenterTest extends TestCase {

    public void testBinaryPattern() {
        Pattern pattern = StreamReader.NON_PRINTABLE;
        assertFalse(pattern.matcher("\tAndrey\r\n").find());
        assertTrue(pattern.matcher("\u0005Andrey").find());
    }

    public void testFloat() {
        assertEquals("1.0E12", String.valueOf(new Double("1e12")));
    }

    public void testNumber() {
        List<Number> list = new ArrayList<Number>();
        list.add(new Byte((byte) 3));
        list.add(new Short((short) 4));
        list.add(new Integer(5));
        list.add(new BigInteger("6"));
        list.add(new Long(7L));
        list.add(Double.POSITIVE_INFINITY);
        list.add(Double.NEGATIVE_INFINITY);
        list.add(Double.NaN);
        Yaml yaml = new Yaml();
        String output = yaml.dump(list);
        assertEquals("[3, 4, 5, 6, 7, .inf, -.inf, .NaN]\n", output);
    }

    public void testDate() {
        List<Date> list = new ArrayList<Date>();
        list.add(new Date(1229684761159L));
        list.add(new Date(1229684761059L));
        list.add(new Date(1229684761009L));
        list.add(new Date(1229684761150L));
        list.add(new Date(1229684761100L));
        list.add(new Date(1229684761000L));
        list.add(new Date(1229684760000L));
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(list);
        assertEquals(
                "- 2008-12-19T11:06:01.159Z\n- 2008-12-19T11:06:01.059Z\n- 2008-12-19T11:06:01.009Z\n- 2008-12-19T11:06:01.150Z\n- 2008-12-19T11:06:01.100Z\n- 2008-12-19T11:06:01Z\n- 2008-12-19T11:06:00Z\n",
                output);
    }

    public void testEmptyArray() {
        Yaml yaml = new Yaml();
        String output = yaml.dump(new String[0]);
        assertEquals("[]\n", output);
    }

    public void testStyle() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(new Integer(1));
        list.add(new Integer(1));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        map.put("name", "Ubuntu");
        map.put("age", 5);
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        assertTrue(output.contains("\"age\": !!int \"5\""));
        assertTrue(output.contains("\"name\": \"Ubuntu\""));
        assertTrue(output.contains("- !!int \"1\""));
    }

    public void testStyle2() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(new Integer(1));
        list.add(new Integer(1));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("age", 5);
        map.put("name", "Ubuntu");
        map.put("list", list);
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        assertEquals("{'age': !!int '5', 'name': 'Ubuntu', 'list': [!!int '1', !!int '1']}\n",
                output);
    }

    public void testStyle2Pretty() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(new Integer(1));
        list.add(new Integer(1));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("age", 5);
        map.put("name", "Ubuntu");
        map.put("list", list);
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(map);
        assertEquals(
                "{\n  'age': !!int '5',\n  'name': 'Ubuntu',\n  'list': [\n    !!int '1',\n    !!int '1']\n  \n}\n",
                output);
    }
}
