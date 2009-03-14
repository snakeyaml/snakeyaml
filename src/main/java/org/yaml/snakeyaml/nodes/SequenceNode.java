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

    public SequenceNode(String tag, List<Node> value, Mark startMark, Mark endMark,
            Boolean flowStyle) {
        super(tag, value, startMark, endMark, flowStyle);
        listType = Object.class;
    }

    public SequenceNode(String tag, List<Node> value, Boolean flowStyle) {
        this(tag, value, null, null, flowStyle);
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.sequence;
    }

    @SuppressWarnings("unchecked")
    public List<Node> getValue() {
        List<Node> value = (List<Node>) super.getValue();
        for (Node node : value) {
            node.setType(listType);
        }
        return value;
    }

    public void setListType(Class<? extends Object> listType) {
        this.listType = listType;
    }
}
