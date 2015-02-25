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

public class ScalarNodeTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testGetNodeId() {
        Node node = new ScalarNode(new Tag("str"), "text", null, null, '>');
        assertEquals(NodeId.scalar, node.getNodeId());
    }

    public void testToString() {
        Node node = new ScalarNode(new Tag("str"), "text", null, null, '>');
        assertTrue(node.toString().contains("ScalarNode"));
        assertTrue(node.toString().contains("tag="));
        assertTrue(node.toString().contains("value="));
    }
}
