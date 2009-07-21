/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

public class NodeTuple {

    private final Node keyNode;
    private final Node valueNode;

    public NodeTuple(Node keyNode, Node valueNode) {
        this.keyNode = keyNode;
        this.valueNode = valueNode;
    }

    public Node getKeyNode() {
        return keyNode;
    }

    public Node getValueNode() {
        return valueNode;
    }
}
