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

import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * Test Chapter 2.5 from the YAML specification
 * 
 * @see http://yaml.org/spec/1.1/
 */
public class Chapter2_5Test extends TestCase {

    @SuppressWarnings("unchecked")
    public void testExample_2_28() {
        YamlStream resource = new YamlStream("example2_28.yaml");
        List<Object> list = (List<Object>) resource.getNativeData();
        assertEquals(3, list.size());
        Map<String, Object> data0 = (Map<String, Object>) list.get(0);
        Date date = (Date) data0.get("Time");
        assertEquals("Date: " + date, 1006545702000L, date.getTime());
        assertEquals("ed", data0.get("User"));
        assertEquals("This is an error message for the log file", data0.get("Warning"));
        //
        Map<String, Object> data1 = (Map<String, Object>) list.get(1);
        Date date1 = (Date) data1.get("Time");
        assertTrue("Date: " + date1, date1.after(date));
        assertEquals("ed", data1.get("User"));
        assertEquals("A slightly different error message.", data1.get("Warning"));
        //
        Map<String, Object> data3 = (Map<String, Object>) list.get(2);
        Date date3 = (Date) data3.get("Date");
        assertTrue("Date: " + date3, date3.after(date1));
        assertEquals("ed", data3.get("User"));
        assertEquals("Unknown variable \"bar\"", data3.get("Fatal"));
        List<Map<String, String>> list3 = (List<Map<String, String>>) data3.get("Stack");
        Map<String, String> map1 = list3.get(0);
        assertEquals("TopClass.py", map1.get("file"));
        assertEquals(new Integer(23), map1.get("line"));
        assertEquals("x = MoreObject(\"345\\n\")\n", map1.get("code"));
        Map<String, String> map2 = list3.get(1);
        assertEquals("MoreClass.py", map2.get("file"));
        assertEquals(new Integer(58), map2.get("line"));
        assertEquals("foo = bar", map2.get("code"));
    }
}
