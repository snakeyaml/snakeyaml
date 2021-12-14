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
package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

public class NodeTest extends TestCase {

    public void testNode() {
        try {
            new ScalarNode(new Tag("!foo"), null, null, null,  DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            fail("Value must be required.");
        } catch (Exception e) {
            assertEquals("value in a Node is required.", e.getMessage());
        }
    }

    public void testSetTag() {
        try {
            ScalarNode node = new ScalarNode(new Tag("!foo"), "Value1", null, null,  DumperOptions.ScalarStyle.DOUBLE_QUOTED);
            node.setTag((Tag) null);
            fail("Value must be required.");
        } catch (Exception e) {
            assertEquals("tag in a Node is required.", e.getMessage());
        }
    }

    public void testGetEndMark() {
        Mark mark1 = new Mark("name", 0, 2, 12, "afd asd asd".toCharArray(), 7);
        Mark mark2 = new Mark("name", 0, 3, 13, "afd asd asd".toCharArray(), 8);
        Node node = new ScalarNode(new Tag("!foo"), "bla-bla", mark1, mark2,  DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        assertEquals(mark1, node.getStartMark());
        assertEquals(mark2, node.getEndMark());
    }

}
