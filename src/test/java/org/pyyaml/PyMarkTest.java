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
package org.pyyaml;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see imported from PyYAML
 */
public class PyMarkTest extends PyImportTest {

    public void testMarks() {
        String content = getResource("test_mark.marks");
        String[] inputs = content.split("---\n");
        for (int i = 1; i < inputs.length; i++) {
            String input = inputs[i];
            int index = 0;
            int line = 0;
            int column = 0;
            while (input.charAt(index) != '*') {
                if (input.charAt(index) != '\n') {
                    line += 1;
                    column = 0;
                } else {
                    column += 1;
                }
                index += 1;
            }
            Mark mark = new Mark("testMarks", index, line, column, input, index);
            String snippet = mark.get_snippet(2, 79);
            assertTrue("Must only have one '\n'.", snippet.indexOf("\n") > -1);
            assertEquals("Must only have only one '\n'.", snippet.indexOf("\n"),
                    snippet.lastIndexOf("\n"));
            String[] lines = snippet.split("\n");
            String data = lines[0];
            String pointer = lines[1];
            assertTrue("Mark must be restricted: " + data, data.length() < 82);
            int dataPosition = data.indexOf("*");
            int pointerPosition = pointer.indexOf("^");
            assertEquals("Pointer should coincide with '*':\n " + snippet, dataPosition,
                    pointerPosition);
        }
    }
}
