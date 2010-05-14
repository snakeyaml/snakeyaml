/**
 * Copyright (c) 2008-2010 Andrey Somov
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

package org.yaml.snakeyaml.issues.issue67;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class NonAsciiCharsInClassNameTest extends TestCase {
    public void testDump() {
        Académico obj = new Académico();
        obj.setId(1);
        obj.setName("Foo bar baz");
        Yaml yaml = new Yaml();
        String result = yaml.dump(obj);
        assertEquals(
                "!!org.yaml.snakeyaml.issues.issue67.NonAsciiCharsInClassNameTest$Acad%C3%A9mico {\n  id: 1, name: Foo bar baz}\n",
                result);
    }

    public void testLoad() {
        Yaml yaml = new Yaml();
        Académico obj = (Académico) yaml
                .load("!!org.yaml.snakeyaml.issues.issue67.NonAsciiCharsInClassNameTest$Acad%C3%A9mico {id: 3, name: Foo bar}");
        assertEquals(3, obj.getId());
        assertEquals("Foo bar", obj.getName());
    }

    public static class Académico {
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private int id;
        private String name;
    }
}
