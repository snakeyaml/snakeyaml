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
    private String value;

    public ScalarNode(String tag, String value, Mark startMark, Mark endMark, Character style) {
        this(tag, false, value, startMark, endMark, style);
    }

    public ScalarNode(String tag, boolean explicit, String value, Mark startMark, Mark endMark,
            Character style) {
        super(tag, startMark, endMark);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = value;
        this.style = style;
        this.explicitTag = explicit;
    }

    public Character getStyle() {
        return style;
    }

    @Override
    public NodeId getNodeId() {
        return NodeId.scalar;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue()
                + ")>";
    }
}
