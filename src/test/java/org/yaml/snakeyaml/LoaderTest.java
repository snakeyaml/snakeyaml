/**
 * Copyright (c) 2008-2014, http://www.snakeyaml.org
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
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

public class LoaderTest extends TestCase {

    public void testCompose1() {
        Yaml loader = new Yaml();
        String yaml = "abc: 3";
        MappingNode node = (MappingNode) loader.compose(new StringReader(yaml));
        List<NodeTuple> nodes = node.getValue();
        assertEquals(1, nodes.size());
        NodeTuple pairs = nodes.get(0);
        ScalarNode key = (ScalarNode) pairs.getKeyNode();
        assertEquals(Tag.STR, key.getTag());
        assertEquals("abc", key.getValue());
        //
        ScalarNode value = (ScalarNode) pairs.getValueNode();
        assertEquals(Tag.INT, value.getTag());
        assertEquals("3", value.getValue());
        //
        assertEquals(
                "<org.yaml.snakeyaml.nodes.MappingNode (tag=tag:yaml.org,2002:map, values={ key=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=abc)>; value=<NodeTuple keyNode=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:str, value=abc)>; valueNode=<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:int, value=3)>> })>",
                node.toString());
    }

    @SuppressWarnings("deprecation")
    public void testCompose2() {
        LoaderOptions options = new LoaderOptions();
        Yaml loader = new Yaml(options);
        String yaml = "3";
        ScalarNode node = (ScalarNode) loader.compose(new StringReader(yaml));
        assertEquals(Tag.INT, node.getTag());
        assertEquals("3", node.getValue());
        // not sure whether it should be null or 0
        assertEquals(Character.valueOf('\u0000'), node.getStyle());
        assertEquals(Object.class, node.getType());
        Mark mark = node.getStartMark();
        assertEquals(0, mark.getColumn());
        assertEquals(0, mark.getLine());
        assertEquals("'reader'", mark.getName());
        assertEquals("    3\n    ^", mark.get_snippet());
        assertEquals("<org.yaml.snakeyaml.nodes.ScalarNode (tag=tag:yaml.org,2002:int, value=3)>",
                node.toString());
    }

    public void testComposeAll() {
        Yaml loader = new Yaml();
        String yaml = "abc: 3\n---\n2\n---\n- qwe\n- asd\n";
        int counter = 0;
        for (Node node : loader.composeAll(new StringReader(yaml))) {
            assertNotNull(node);
            switch (counter++) {
            case 0:
                assertEquals(NodeId.mapping, node.getNodeId());
                break;
            case 1:
                assertEquals(NodeId.scalar, node.getNodeId());
                break;
            case 2:
                assertEquals(NodeId.sequence, node.getNodeId());
                break;
            default:
                fail("Unexpected document.");
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void testDeprecated() {
        Yaml yaml = new Yaml(new Loader());
        yaml.load("123");
        yaml = new Yaml(new Loader(), new Dumper());
        yaml.load("123");
        yaml = new Yaml(new Loader(), new Dumper(), new Resolver());
        yaml.load("123");
    }
}
