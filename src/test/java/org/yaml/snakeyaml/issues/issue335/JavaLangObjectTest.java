/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue335;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.*;

public class JavaLangObjectTest {

    @Test
    public void testLoadObjectAsMapping() throws Exception {
        Object obj = new Yaml().load("!!java.lang.Object {}");
        assertEquals(Object.class, obj.getClass());
    }

    @Test
    public void testLoadObjectAsScalar() throws Exception {
        try {
            new Yaml().load("!!java.lang.Object");
            fail("Object has no single argument constructor");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("No single argument constructor found"));
        }
    }
}