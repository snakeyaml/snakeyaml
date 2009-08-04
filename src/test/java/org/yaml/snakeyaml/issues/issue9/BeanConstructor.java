/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue9;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;

public class BeanConstructor extends Constructor {

    public BeanConstructor() {
        super(BeanHolder.class);
        yamlClassConstructors.put(NodeId.scalar, new BeanScalarConstructor());
    }

    private class BeanScalarConstructor extends ConstructScalar {
        @Override
        public Object construct(Node node) {
            ScalarNode snode = (ScalarNode) node;
            if (BeanHolder.class.equals(node.getType()) || IBean.class.equals(node.getType())) {
                if (snode.getValue().length() == 0) {
                    try {
                        return getClassForNode(node).newInstance();
                    } catch (Exception e) {
                        throw new YAMLException("BeanHolder cannot be created");
                    }
                } else {
                    throw new YAMLException("BeanHolder cannot be created out of '"
                            + snode.getValue() + "'");
                }
            } else {
                return super.construct(node);
            }
        }
    }
}
