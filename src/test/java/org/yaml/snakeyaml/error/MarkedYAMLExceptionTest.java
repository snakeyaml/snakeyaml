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

public class MarkedYAMLExceptionTest extends TestCase {

    public void testToString1() {
        Mark mark = new Mark("test1", 0, 0, 0, "*The first line.\nThe last line.", 0);
        MarkedYAMLException exception = new MarkedYAMLException(null, null, "Error happened", mark);
        assertTrue(exception.toString().contains("Error happened"));
        assertTrue(exception.toString().contains("The first line"));
        assertTrue(exception.toString(), exception.toString().contains("test1"));
    }

    public void testToString2() {
        Mark mark = new Mark("search", 0, 0, 0, "*The first line.\nThe last line.", 0);
        MarkedYAMLException exception = new MarkedYAMLException("See http://www.google.com", mark,
                "Error2 happened", mark);
        assertTrue(exception.toString().contains("Error2 happened"));
        assertTrue(exception.toString().contains("The first line"));
        assertTrue(exception.toString().contains("search"));
    }

    public void testToString3() {
        MarkedYAMLException exception = new MarkedYAMLException("See http://www.google.com", null,
                null, null, "Note1");
        assertTrue(exception.toString().contains("Note1"));
    }

    public void testToString4() {
        Mark mark = new Mark("search", 0, 0, 0, "*The first line.\nThe last line.", 0);
        MarkedYAMLException exception = new MarkedYAMLException("See http://www.google.com", mark,
                null, null, null, null);
        assertTrue(exception.toString().contains("first line"));
    }

    public void testGetters() {
        Mark mark = new Mark("search", 0, 0, 0, "*The first line.\nThe last line.", 0);
        MarkedYAMLException exception = new MarkedYAMLException("See http://www.google.com", mark,
                "Error2 happened", mark);
        assertEquals("See http://www.google.com", exception.getContext());
        assertEquals(mark, exception.getContextMark());
        assertEquals("Error2 happened", exception.getProblem());
        assertEquals(mark, exception.getProblemMark());
    }
}
