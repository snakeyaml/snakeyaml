/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public abstract class Node {
    private String tag;
    protected Object value;
    private Mark startMark;
    protected Mark endMark;
    private Class<? extends Object> type;

    public Node(String tag, Object value, Mark startMark, Mark endMark) {
        setTag(tag);
        if (value == null) {
            throw new NullPointerException("value in a Node is required.");
        }
        this.value = value;
        this.startMark = startMark;
        this.endMark = endMark;
        this.type = Object.class;
    }

    public String getTag() {
        return this.tag;
    }

    public Object getValue() {
        return this.value;
    }

    public Mark getEndMark(){
    	return endMark;
    }

    public String toString() {
        return "<" + this.getClass().getName() + " (tag=" + getTag() + ", value=" + getValue()
                + ")>";
    }

    /**
     * For error reporting.
     * 
     * @see class variable 'id' in PyYAML
     * @return scalar, sequence, mapping
     */
    public abstract NodeId getNodeId();

    public Mark getStartMark() {
        return startMark;
    }

    public void setTag(String tag) {
        if (tag == null) {
            throw new NullPointerException("tag in a Node is required.");
        }
        this.tag = tag;
    }

    /*
     * It is not allowed to overwrite this method. Two Nodes are never equal.
     */
    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Class<? extends Object> getType() {
        return type;
    }

    public void setType(Class<? extends Object> type) {
        this.type = type;
    }
}
