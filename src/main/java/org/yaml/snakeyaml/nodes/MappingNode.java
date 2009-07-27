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
    private List<NodeTuple> value;

    public MappingNode(String tag, List<NodeTuple> value, Mark startMark, Mark endMark,
            Boolean flowStyle) {
        super(tag, startMark, endMark, flowStyle);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = value;
        keyType = Object.class;
        valueType = Object.class;
    }

    public MappingNode(String tag, List<NodeTuple> value, Boolean flowStyle) {
        this(tag, value, null, null, flowStyle);
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.mapping;
    }

    public List<NodeTuple> getValue() {
        for (NodeTuple nodes : value) {
            nodes.getKeyNode().setType(keyType);
            nodes.getValueNode().setType(valueType);
        }
        return value;
    }

    public void setValue(List<NodeTuple> merge) {
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
        for (NodeTuple node : getValue()) {
            buf.append("{ key=");
            buf.append(node.getKeyNode());
            buf.append("; value=Node<");
            // to avoid overflow in case of recursive structures
            buf.append(System.identityHashCode(node.getValueNode()));
            buf.append("> }");
        }
        values = buf.toString();
        return "<" + this.getClass().getName() + " (tag=" + getTag() + ", values=" + values + ")>";
    }
}
