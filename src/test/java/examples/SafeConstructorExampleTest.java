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
package examples;

import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class SafeConstructorExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testConstruct() {
        String doc = "- 5\n- Person\n- true";
        Yaml yaml = new Yaml(new SafeConstructor());
        List<Object> list = (List<Object>) yaml.load(doc);
        assertEquals(3, list.size());
        assertEquals(new Integer(5), list.get(0));
        assertEquals("Person", list.get(1));
        assertEquals(Boolean.TRUE, list.get(2));
    }

    public void testSafeConstruct() {
        String doc = "- 5\n- !org.yaml.snakeyaml.constructor.Person\n  firstName: Andrey\n  age: 99\n- true";
        Yaml yaml = new Yaml(new SafeConstructor());
        try {
            yaml.load(doc);
            fail("Custom Java classes should not be created.");
        } catch (Exception e) {
            assertEquals(
                    "could not determine a constructor for the tag !org.yaml.snakeyaml.constructor.Person\n"
                            + " in 'string', line 2, column 3:\n"
                            + "    - !org.yaml.snakeyaml.constructor. ... \n" + "      ^\n",
                    e.getMessage());
        }
    }
}
