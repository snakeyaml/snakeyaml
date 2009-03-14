/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class ScalarNode extends Node {
    private Character style;

    public ScalarNode(String tag, String value, Mark startMark, Mark endMark, Character style) {
        super(tag, value, startMark, endMark);
        this.style = style;
    }

    public Character getStyle() {
        return style;
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.scalar;
    }
}
