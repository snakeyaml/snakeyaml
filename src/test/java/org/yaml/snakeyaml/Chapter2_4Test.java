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
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Test Chapter 2.4 from the YAML specification
 * 
 * @see http://yaml.org/spec/1.1/
 */
public class Chapter2_4Test extends TestCase {

    @SuppressWarnings("unchecked")
    public void testExample_2_19() {
        YamlDocument document = new YamlDocument("example2_19.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(5, map.size());
        assertEquals("Expect 12345 to be an Integer.", Integer.class, map.get("canonical")
                .getClass());
        assertEquals(new Integer(12345), map.get("canonical"));
        assertEquals(new Integer(12345), map.get("decimal"));
        assertEquals(new Integer(3 * 3600 + 25 * 60 + 45), map.get("sexagesimal"));
        assertEquals(new Integer(014), map.get("octal"));
        assertEquals(new Integer(0xC), map.get("hexadecimal"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_20() {
        YamlDocument document = new YamlDocument("example2_20.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(6, map.size());
        assertEquals("Expect '1.23015e+3' to be a Double.", Double.class, map.get("canonical")
                .getClass());
        assertEquals(new Double(1230.15), map.get("canonical"));
        assertEquals(new Double(12.3015e+02), map.get("exponential"));
        assertEquals(new Double(20 * 60 + 30.15), map.get("sexagesimal"));
        assertEquals(new Double(1230.15), map.get("fixed"));
        assertEquals(Double.NEGATIVE_INFINITY, map.get("negative infinity"));
        assertEquals(Double.NaN, map.get("not a number"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_21() {
        YamlDocument document = new YamlDocument("example2_21.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(4, map.size());
        assertNull("'~' must be parsed as 'null': " + map.get(null), map.get(null));
        assertTrue((Boolean) map.get(Boolean.TRUE));
        assertFalse((Boolean) map.get(Boolean.FALSE));
        assertEquals("12345", map.get("string"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_22() {
        YamlDocument document = new YamlDocument("example2_22.yaml");
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(4, map.size());
        assertEquals("Expect '2001-12-15T02:59:43.1Z' to be a Date.", Date.class,
                map.get("canonical").getClass());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.clear();
        cal.set(Calendar.YEAR, 2001);
        cal.set(Calendar.MONTH, 11); // Java's months are zero-based...
        cal.set(Calendar.DAY_OF_MONTH, 15);
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 43);
        cal.set(Calendar.MILLISECOND, 100);
        Date date = cal.getTime();
        assertEquals(date, map.get("canonical"));
        assertEquals("Expect '2001-12-14t21:59:43.10-05:00' to be a Date.", Date.class,
                map.get("iso8601").getClass());
        assertEquals("Expect '2001-12-14 21:59:43.10 -5' to be a Date.", Date.class,
                map.get("spaced").getClass());
        assertEquals("Expect '2002-12-14' to be a Date.", Date.class, map.get("date").getClass());
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_23_non_date() {
        try {
            YamlDocument document = new YamlDocument("example2_23_non_date.yaml");
            Map<String, Object> map = (Map<String, Object>) document.getNativeData();
            assertEquals(1, map.size());
            assertEquals("2002-04-28", map.get("not-date"));
        } catch (RuntimeException e) {
            fail("Cannot parse '!!str': 'not-date: !!str 2002-04-28'");
        }
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_23_picture() {
        YamlDocument document = new YamlDocument("example2_23_picture.yaml", false);
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(1, map.size());
        byte[] picture = (byte[]) map.get("picture");
        assertEquals((byte) 'G', picture[0]);
        assertEquals((byte) 'I', picture[1]);
        assertEquals((byte) 'F', picture[2]);
    }

    class SomethingConstructor extends Constructor {
        public SomethingConstructor() {
            this.yamlConstructors.put(new Tag("!something"), new ConstructSomething());
        }

        private class ConstructSomething extends AbstractConstruct {
            public Object construct(Node node) {
                // convert to upper case
                String val = (String) constructScalar((ScalarNode) node);
                return val.toUpperCase().replace('\n', ' ').trim();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_23() {
        YamlDocument document = new YamlDocument("example2_23.yaml", false,
                new SomethingConstructor());
        Map<String, Object> map = (Map<String, Object>) document.getNativeData();
        assertEquals(3, map.size());
        String special = (String) map.get("application specific tag");
        assertEquals("THE SEMANTICS OF THE TAG ABOVE MAY BE DIFFERENT FOR DIFFERENT DOCUMENTS.",
                special);
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_25() {
        YamlDocument document = new YamlDocument("example2_25.yaml");
        Set<String> set = (Set<String>) document.getNativeData();
        assertEquals(3, set.size());
        assertTrue(set.contains("Mark McGwire"));
        assertTrue(set.contains("Sammy Sosa"));
        assertTrue(set.contains("Ken Griff"));
    }

    @SuppressWarnings("unchecked")
    public void testExample_2_26() {
        YamlDocument document = new YamlDocument("example2_26.yaml");
        Map<String, String> map = (Map<String, String>) document.getNativeData();
        assertEquals(3, map.size());
        assertTrue(map instanceof LinkedHashMap);
        assertEquals(new Integer(65), map.get("Mark McGwire"));
        assertEquals(new Integer(63), map.get("Sammy Sosa"));
        assertEquals(new Integer(58), map.get("Ken Griffy"));
        List<String> list = new ArrayList<String>();
        for (String key : map.keySet()) {
            list.add(key);
        }
        assertEquals("Mark McGwire", list.get(0));
        assertEquals("Sammy Sosa", list.get(1));
        assertEquals("Ken Griffy", list.get(2));
    }
}
