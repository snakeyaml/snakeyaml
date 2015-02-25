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
package org.yaml.snakeyaml.constructor;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class ConstructorTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testMapOrder() {
        String data = "one: zzz\ntwo: ccc\nthree: bbb\nfour: aaa";
        Object map = construct(data);
        assertNotNull(map);
        assertTrue(map.getClass().toString(), map instanceof LinkedHashMap);
        Map<String, String> m = (Map<String, String>) map;
        assertEquals(4, m.keySet().size());
        Iterator<String> iter = m.keySet().iterator();
        assertEquals("one", iter.next());
        assertEquals("two", iter.next());
        assertEquals("three", iter.next());
        assertEquals("four", iter.next());
    }

    /**
     * create JavaBean
     */
    public void testGetBeanAssumeClass() {
        String data = "--- !!org.yaml.snakeyaml.constructor.Person\nfirstName: Andrey\nage: 99";
        Object obj = construct(data);
        assertNotNull(obj);
        assertTrue("Unexpected: " + obj.getClass().toString(), obj instanceof Person);
        Person person = (Person) obj;
        assertEquals("Andrey", person.getFirstName());
        assertNull(person.getLastName());
        assertEquals(99, person.getAge().intValue());
    }

    /**
     * create instance using constructor arguments
     */
    public void testGetConstructorBean() {
        String data = "--- !!org.yaml.snakeyaml.constructor.Person [ Andrey, Somov, 99 ]";
        Object obj = construct(data);
        assertNotNull(obj);
        assertTrue(obj.getClass().toString(), obj instanceof Person);
        Person person = (Person) obj;
        assertEquals("Andrey", person.getFirstName());
        assertEquals("Somov", person.getLastName());
        assertEquals(99, person.getAge().intValue());
    }

    /**
     * create instance using scalar argument
     */
    public void testGetConstructorFromScalar() {
        String data = "--- !!org.yaml.snakeyaml.constructor.Person 'Somov'";
        Object obj = construct(data);
        assertNotNull(obj);
        assertTrue(obj.getClass().toString(), obj instanceof Person);
        Person person = (Person) obj;
        assertNull("Andrey", person.getFirstName());
        assertEquals("Somov", person.getLastName());
        assertNull(person.getAge());
    }

    public void testJavaBeanLoad() {
        java.util.Calendar cal = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.clear();
        cal.set(1982, 5 - 1, 3); // Java's months are zero-based...

        TestBean expected = new TestBean("Ola Bini", 24, cal.getTime());
        assertEquals(
                expected,
                construct("--- !!org.yaml.snakeyaml.constructor.TestBean\nname: Ola Bini\nage: 24\nborn: 1982-05-03\n"));
    }

    public void testWrongName() {
        try {
            construct("--- !!org.yaml.snakeyaml.constructor.TestBean\nwrongName: No one\nage: 24\nborn: 1982-05-03\n");
            fail("IntrospectionException expected.");
        } catch (Exception e) {
            // TODO improve the error message - the pointer should be at the
            // property name, not value
            assertEquals(
                    "Cannot create property=wrongName for JavaBean=#<org.jvyaml.TestBean name=\"null\" age=0 born=\"null\">\n"
                            + " in 'string', line 1, column 5:\n"
                            + "    --- !!org.yaml.snakeyaml.constructor ... \n"
                            + "        ^\n"
                            + "Unable to find property 'wrongName' on class: org.yaml.snakeyaml.constructor.TestBean\n"
                            + " in 'string', line 2, column 12:\n"
                            + "    wrongName: No one\n"
                            + "               ^\n", e.getMessage());
        }
    }

    private Object construct(String data) {
        Yaml yaml = new Yaml();
        return yaml.load(data);
    }
}
