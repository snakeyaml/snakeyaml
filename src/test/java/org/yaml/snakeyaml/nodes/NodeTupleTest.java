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

import junit.framework.TestCase;

public class NodeTupleTest extends TestCase {

    public void testNodeTuple1() {
        Node node = new ScalarNode(new Tag("!tag"), "value1", null, null, null);
        try {
            new NodeTuple(null, node);
            fail("Node must be provided.");
        } catch (Exception e) {
            assertEquals("Nodes must be provided.", e.getMessage());
        }
    }

    public void testNodeTuple2() {
        Node node = new ScalarNode(new Tag("!tag"), "value1", null, null, null);
        try {
            new NodeTuple(node, null);
            fail("Node must be provided.");
        } catch (Exception e) {
            assertEquals("Nodes must be provided.", e.getMessage());
        }
    }

    public void testToString() {
        Node key = new ScalarNode(Tag.STR, "key1", null, null, null);
        Node value = new ScalarNode(Tag.STR, "value1", null, null, null);
        NodeTuple tuple = new NodeTuple(key, value);
        assertEquals(
                "<NodeTuple keyNode=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=key1)>; valueNode=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=value1)>>",
                tuple.toString());
    }

}
