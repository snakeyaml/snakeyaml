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
package org.yaml.snakeyaml.partialconstruct;

import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.resolver.Resolver;

class FragmentComposer extends Composer {
    String nodeName;

    public FragmentComposer(Parser parser, Resolver resolver, String nodeName) {
        super(parser, resolver);
        this.nodeName = nodeName;
    }

    @Override
    public Node getSingleNode() {
        Node node = super.getSingleNode();
        if (!MappingNode.class.isAssignableFrom(node.getClass())) {
            throw new RuntimeException(
                    "Document is not structured as expected.  Root element should be a map!");
        }
        MappingNode root = (MappingNode) node;
        for (NodeTuple tuple : root.getValue()) {
            Node keyNode = tuple.getKeyNode();
            if (ScalarNode.class.isAssignableFrom(keyNode.getClass())) {
                if (((ScalarNode) keyNode).getValue().equals(nodeName)) {
                    return tuple.getValueNode();
                }
            }
        }
        throw new RuntimeException("Did not find key \"" + nodeName + "\" in document-level map");
    }
}