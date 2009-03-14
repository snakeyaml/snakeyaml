/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public abstract class CollectionStartEvent extends NodeEvent {
    private final String tag;
    private final boolean implicit;
    private final Boolean flowStyle;

    public CollectionStartEvent(String anchor, String tag, boolean implicit, Mark startMark,
            Mark endMark, Boolean flowStyle) {
        super(anchor, startMark, endMark);
        this.tag = tag;
        this.implicit = implicit;
        this.flowStyle = flowStyle;
    }

    public String getTag() {
        return this.tag;
    }

    public boolean getImplicit() {
        return this.implicit;
    }

    public Boolean getFlowStyle() {
        return this.flowStyle;
    }

    @Override
    protected String getArguments() {
        return super.getArguments() + ", tag=" + tag + ", implicit=" + implicit;
    }
}
