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
package org.yaml.snakeyaml.issues.issue9;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class BeanConstructor extends Constructor {

    public BeanConstructor() {
        super(BeanHolder.class);
        yamlConstructors.put(new Tag(Bean1.class), new Bean1ScalarConstructor());
        yamlConstructors.put(new Tag(BeanHolder.class), new BeanHolderScalarConstructor());
    }

    private class Bean1ScalarConstructor extends ConstructScalar {
        @Override
        public Object construct(Node node) {
            ScalarNode snode = (ScalarNode) node;
            if (snode.getValue().length() == 0) {
                return new Bean1();
            } else {
                return new Bean1(new Integer(snode.getValue()));
            }
        }
    }

    private class BeanHolderScalarConstructor extends ConstructScalar {
        @Override
        public Object construct(Node node) {
            if (node.getNodeId() == NodeId.scalar) {
                ScalarNode n = (ScalarNode) node;
                String value = n.getValue();
                int i = 3;
                if (value.length() != 0) {
                    i = Integer.parseInt(value);
                }
                return new BeanHolder(new Bean1(i));
            } else {
                return new BeanHolder();
            }
        }
    }
}
