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

import java.util.Iterator;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.YAMLException;

public class YamlTest extends TestCase {

    public void testSetNoName() {
        Yaml yaml = new Yaml();
        assertTrue(yaml.toString().matches("Yaml:\\d+"));
    }

    public void testSetName() {
        Yaml yaml = new Yaml();
        yaml.setName("REST");
        assertEquals("REST", yaml.getName());
        assertEquals("REST", yaml.toString());
    }

    /**
     * Check that documents are parsed only when they are asked to be loaded.
     */
    public void testOneDocument() {
        Yaml yaml = new Yaml();
        String doc = "--- a\n--- [:]";
        Iterator<Object> loaded = yaml.loadAll(doc).iterator();
        assertTrue(loaded.hasNext());
        Object obj1 = loaded.next();
        assertEquals("a", obj1);
        assertTrue(loaded.hasNext());
        try {
            loaded.next();
            fail("Second document is invalid");
        } catch (Exception e) {
            assertEquals("while parsing a flow node\n" + " in 'reader', line 2, column 6:\n"
                    + "    --- [:]\n" + "         ^\n"
                    + "expected the node content, but found Value\n"
                    + " in 'reader', line 2, column 6:\n" + "    --- [:]\n" + "         ^\n",
                    e.getMessage());
        }
    }

    public void testOnlyOneDocument() {
        Yaml yaml = new Yaml();
        String doc = "--- a\n--- b";
        try {
            yaml.load(doc);
            fail("It must be only one document.");
        } catch (YAMLException e) {
            assertEquals("expected a single document in the stream\n"
                    + " in 'string', line 1, column 5:\n" + "    --- a\n" + "        ^\n"
                    + "but found another document\n" + " in 'string', line 2, column 1:\n"
                    + "    --- b\n" + "    ^\n", e.getMessage());
        }
    }
}
