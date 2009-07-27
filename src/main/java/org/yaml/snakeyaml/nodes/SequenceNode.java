/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import java.util.List;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class SequenceNode extends CollectionNode {
    private Class<? extends Object> listType;
    private List<Node> value;

    public SequenceNode(String tag, List<Node> value, Mark startMark, Mark endMark,
            Boolean flowStyle) {
        super(tag, startMark, endMark, flowStyle);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = value;
        listType = Object.class;
    }

    public SequenceNode(String tag, List<Node> value, Boolean flowStyle) {
        this(tag, value, null, null, flowStyle);
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.sequence;
    }

    public List<Node> getValue() {
        for (Node node : value) {
            node.setType(listType);
        }
        return value;
    }

    public void setListType(Class<? extends Object> listType) {
        this.listType = listType;
    }

    public String toString() {
        return "<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue()
                + ")>";
    }
}
