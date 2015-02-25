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
package org.yaml.snakeyaml.nodes;

import java.util.ArrayList;

import junit.framework.TestCase;

public class SequenceNodeTest extends TestCase {

    public void testGetNodeId() {
        SequenceNode node = new SequenceNode(new Tag("!foo"), true, new ArrayList<Node>(), null,
                null, true);
        assertEquals(NodeId.sequence, node.getNodeId());
    }

    public void testNullValue() {
        try {
            new SequenceNode(new Tag("!foo"), true, null, null, null, true);
            fail("Value is required.");
        } catch (Exception e) {
            assertEquals("value in a Node is required.", e.getMessage());
        }
    }
}
