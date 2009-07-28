/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

public class NodeTuple {

    private final Node keyNode;
    private final Node valueNode;

    public NodeTuple(Node keyNode, Node valueNode) {
        if (keyNode == null || valueNode == null) {
            throw new NullPointerException("Nodes must be provided.");
        }
        this.keyNode = keyNode;
        this.valueNode = valueNode;
    }

    public Node getKeyNode() {
        return keyNode;
    }

    public Node getValueNode() {
        return valueNode;
    }

    @Override
    public String toString() {
        return "<NodeTuple keyNode=" + keyNode.toString() + "; valueNode=" + valueNode.toString()
                + ">";
    }
}
