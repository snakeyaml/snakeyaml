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
package org.yaml.snakeyaml.extensions.compactnotation;

import junit.framework.TestCase;

public class PackageCompactConstructorTest extends TestCase {

    public void testGetClassForName() throws ClassNotFoundException {
        assertEquals(Table.class, check("Table"));
        assertEquals(Table.class, check("org.yaml.snakeyaml.extensions.compactnotation.Table"));
        assertEquals(String.class, check("java.lang.String"));
    }

    public void testException1() throws ClassNotFoundException {
        try {
            check("foo.Bar");
            fail();
        } catch (ClassNotFoundException e) {
            assertEquals("foo.Bar", e.getMessage());
        }
    }

    public void testException2() throws ClassNotFoundException {
        try {
            check("FooBar");
            fail();
        } catch (ClassNotFoundException e) {
            assertEquals("FooBar", e.getMessage());
        }
    }

    private Class<?> check(String name) throws ClassNotFoundException {
        return new PackageCompactConstructor("org.yaml.snakeyaml.extensions.compactnotation")
                .getClassForName(name);
    }
}
