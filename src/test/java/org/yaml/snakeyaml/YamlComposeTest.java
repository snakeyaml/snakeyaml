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

import java.io.StringReader;

import junit.framework.TestCase;

import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class YamlComposeTest extends TestCase {

    public void testComposeManyDocuments() {
        try {
            Yaml yaml = new Yaml();
            yaml.compose(new StringReader("abc: 56\n---\n123\n---\n456"));
            fail("YAML contans more then one document.");
        } catch (Exception e) {
            assertTrue("<<<" + e.getMessage() + ">>>",
                    e.getMessage().startsWith("expected a single document in the stream"));
        }
    }

    public void testComposeFromReader() {
        Yaml yaml = new Yaml();
        MappingNode node = (MappingNode) yaml.compose(new StringReader("abc: 56"));
        ScalarNode node1 = (ScalarNode) node.getValue().get(0).getKeyNode();
        assertEquals("abc", node1.getValue());
        ScalarNode node2 = (ScalarNode) node.getValue().get(0).getValueNode();
        assertEquals("56", node2.getValue());
    }

    public void testComposeAllFromReader() {
        Yaml yaml = new Yaml();
        boolean first = true;
        for (Node node : yaml.composeAll(new StringReader("abc: 56\n---\n123\n---\n456"))) {
            if (first) {
                assertEquals(NodeId.mapping, node.getNodeId());
            } else {
                assertEquals(NodeId.scalar, node.getNodeId());
            }
            first = false;
        }
    }

    public void testComposeAllOneDocument() {
        Yaml yaml = new Yaml();
        for (Node node : yaml.composeAll(new StringReader("6"))) {
            assertEquals(NodeId.scalar, node.getNodeId());
        }
    }
}
