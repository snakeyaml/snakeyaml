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
package org.yaml.snakeyaml.constructor;

import junit.framework.TestCase;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.ArrayList;

public class AbstractConstructTest extends TestCase {

    public void testNotRecursive() {
        AbstractConstruct abstractConstruct = new AbstractConstruct() {
            public Object construct(Node node) {
                return null;
            }
        };
        Node node = new SequenceNode(Tag.SEQ, true, new ArrayList<Node>(), null, null, false);
        try {
            abstractConstruct.construct2ndStep(node, "");
            fail();
        } catch (YAMLException e) {
            assertEquals("Unexpected recursive structure for Node: <org.yaml.snakeyaml.nodes.SequenceNode (tag=tag:yaml.org,2002:seq, value=[])>", e.getMessage());
        }
    }

    public void testRecursive() {
        AbstractConstruct abstractConstruct = new AbstractConstruct() {

            public Object construct(Node node) {
                return null;
            }
        };
        Node node = new SequenceNode(Tag.SEQ, true, new ArrayList<Node>(), null, null, false);
        node.setTwoStepsConstruction(true);
        try {
            abstractConstruct.construct2ndStep(node, "");
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Not Implemented in org.yaml.snakeyaml.constructor.AbstractConstructTest$2", e.getMessage());
        }
    }
}
