/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import java.util.List;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class MappingNode extends CollectionNode {
    private Class<? extends Object> keyType;
    private Class<? extends Object> valueType;

    public MappingNode(String tag, List<Node[]> value, Mark startMark, Mark endMark,
            Boolean flowStyle) {
        super(tag, value, startMark, endMark, flowStyle);
        keyType = Object.class;
        valueType = Object.class;
    }

    public MappingNode(String tag, List<Node[]> value, Boolean flowStyle) {
        super(tag, value, null, null, flowStyle);
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.mapping;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Node[]> getValue() {
        List<Node[]> mapping = (List<Node[]>) super.getValue();
        for (Node[] nodes : mapping) {
            nodes[0].setType(keyType);
            nodes[1].setType(valueType);
        }
        return mapping;
    }

    public void setValue(List<Node[]> merge) {
        value = merge;
    }

    public void setKeyType(Class<? extends Object> keyType) {
        this.keyType = keyType;
    }

    public void setValueType(Class<? extends Object> valueType) {
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        String values;
        StringBuffer buf = new StringBuffer();
        for (Node[] node : getValue()) {
            buf.append("{ key=");
            buf.append(node[0]);
            buf.append("; value=");
            // to avoid overflow in case of recursive structures
            buf.append(System.identityHashCode(node[1]));
            buf.append(" }");
        }
        values = buf.toString();
        return "<" + this.getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
    }
}
