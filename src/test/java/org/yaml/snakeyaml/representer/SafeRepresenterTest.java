/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class SafeRepresenterTest extends TestCase {

    public void testBinaryPattern() {
        Pattern pattern = SafeRepresenter.BINARY_PATTERN;
        assertFalse(pattern.matcher("\tAndrey\r\n").find());
        assertTrue(pattern.matcher("\u0005Andrey").find());
    }

    public void testFloat() {
        assertEquals("1.0E12", String.valueOf(new Double("1e12")));
    }

    public void testNumber() {
        List<Number> list = new LinkedList<Number>();
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
        List<Date> list = new LinkedList<Date>();
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
        List<Integer> list = new LinkedList<Integer>();
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
        List<Integer> list = new LinkedList<Integer>();
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
}
