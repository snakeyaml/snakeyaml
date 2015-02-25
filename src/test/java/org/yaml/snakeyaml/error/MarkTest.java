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
package org.yaml.snakeyaml.error;

import junit.framework.TestCase;

public class MarkTest extends TestCase {

    public void testGet_snippet() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        assertEquals("    *The first line.\n    ^", mark.get_snippet());
        mark = new Mark("test1", 9, 0, 0, "The first*line.\nThe last line.", 9);
        assertEquals("    The first*line.\n             ^", mark.get_snippet());
    }

    public void testToString() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        String[] lines = mark.toString().split("\n");
        assertEquals(" in test1, line 1, column 1:", lines[0]);
        assertEquals("*The first line.", lines[1].trim());
        assertEquals("^", lines[2].trim());
    }

    public void testPosition() {
        Mark mark = new Mark("test1", 17, 29, 213, "*The first line.\nThe last line.", 0);
        assertEquals(17, mark.getIndex());
        assertEquals(29, mark.getLine());
        assertEquals(213, mark.getColumn());
    }
}
