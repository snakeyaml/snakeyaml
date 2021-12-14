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
package org.yaml.snakeyaml.error;

import junit.framework.TestCase;

public class MarkTest extends TestCase {

    public void testGet_snippet() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.".toCharArray(), 0);
        assertEquals("    *The first line.\n    ^", mark.get_snippet());
        mark = new Mark("test1", 0, 0, 0, "The first*line.\nThe last line.".toCharArray(), 9);
        assertEquals("    The first*line.\n             ^", mark.get_snippet());
    }

    public void testToString() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.".toCharArray(), 0);
        String[] lines = mark.toString().split("\n");
        assertEquals(" in test1, line 1, column 1:", lines[0]);
        assertEquals("*The first line.", lines[1].trim());
        assertEquals("^", lines[2].trim());
    }

    public void testPosition() {
        Mark mark = new Mark("test1", 17, 29, 213, "*The first line.\nThe last line.".toCharArray(), 0);
        assertEquals("index is used in JRuby", 17, mark.getIndex());
        assertEquals(29, mark.getLine());
        assertEquals(213, mark.getColumn());
    }

    public void testGetBuffer() {
        Mark mark = new Mark("test1", 0, 29, 213, "*The first line.\nThe last line.".toCharArray(), 0);
        int[] buffer = new int[] { 42, 84, 104, 101, 32, 102, 105, 114, 115, 116, 32, 108, 105, 110, 101, 46, 10, 84, 104, 101, 32, 108, 97, 115, 116, 32, 108, 105, 110, 101, 46 };
        assertTrue(buffer.length == mark.getBuffer().length);
        boolean match = true;
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != mark.getBuffer()[i]) {
                match = false;
                break;
            }
        }
        assertTrue(match);
    }

    public void testGetPointer() {
        Mark mark = new Mark("test1", 0, 29, 213, "*The first line.\nThe last line.".toCharArray(), 5);
        assertEquals(5, mark.getPointer());
    }
}
