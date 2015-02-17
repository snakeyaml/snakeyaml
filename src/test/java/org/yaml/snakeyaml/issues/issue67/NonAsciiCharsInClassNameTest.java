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
package org.yaml.snakeyaml.issues.issue67;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.scanner.ScannerException;

public class NonAsciiCharsInClassNameTest extends TestCase {
    private String PREFIX = "!!org.yaml.snakeyaml.issues.issue67.NonAsciiCharsInClassNameTest$";

    public void testDump() {
        Académico obj = new Académico();
        obj.setId(1);
        obj.setName("Foo bar baz");
        Yaml yaml = new Yaml();
        String result = yaml.dump(obj);
        assertEquals(PREFIX + "Acad%C3%A9mico {\n  id: 1, name: Foo bar baz}\n", result);
    }

    public void testLoad() {
        Yaml yaml = new Yaml();
        Académico obj = (Académico) yaml.load(PREFIX + "Acad%C3%A9mico {id: 3, name: Foo bar}");
        assertEquals(3, obj.getId());
        assertEquals("Foo bar", obj.getName());
    }

    public void testLoadInvalidPattern() {
        try {
            Yaml yaml = new Yaml();
            yaml.load(PREFIX + "Acad%WZ%A9mico {id: 3, name: Foo bar}");
            fail("Illegal hex characters in escape (%) pattern must not be accepted.");
        } catch (Exception e) {
            assertEquals(
                    "while scanning a tag\n"
                            + " in 'string', line 1, column 1:\n"
                            + "    !!org.yaml.snakeyaml.issues.issu ... \n"
                            + "    ^\n"
                            + "expected URI escape sequence of 2 hexadecimal numbers, but found W(87) and Z(90)\n"
                            + " in 'string', line 1, column 71:\n"
                            + "     ... nAsciiCharsInClassNameTest$Acad%WZ%A9mico {id: 3, name: Foo bar}\n"
                            + "                                         ^\n", e.getMessage());
        }
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

    public void testDumpCustomTag() {
        Académico obj = new Académico();
        obj.setId(123);
        obj.setName("Foo bar 123");
        Representer repr = new Representer();
        repr.addClassTag(Académico.class, new Tag("!foo"));
        Yaml yaml = new Yaml(repr);
        String result = yaml.dump(obj);
        assertEquals("!foo {id: 123, name: Foo bar 123}\n", result);
    }

    public void testDumpEscapedTag() {
        Académico obj = new Académico();
        obj.setId(123);
        obj.setName("Foo bar 123");
        Representer repr = new Representer();
        repr.addClassTag(Académico.class, new Tag("!Académico"));
        Yaml yaml = new Yaml(repr);
        String result = yaml.dump(obj);
        assertEquals("!Acad%C3%A9mico {id: 123, name: Foo bar 123}\n", result);
    }

    public void testTag() {
        Tag tag = new Tag("!java/javabean:foo.Bar");
        assertEquals("!java/javabean:foo.Bar", tag.getValue());
    }
}
