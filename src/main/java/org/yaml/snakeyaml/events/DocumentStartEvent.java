/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.events;

import java.util.Map;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class DocumentStartEvent extends Event {
    private final boolean explicit;
    private final Integer[] version;
    private final Map<String, String> tags;

    public DocumentStartEvent(Mark startMark, Mark endMark, boolean explicit, Integer[] version,
            Map<String, String> tags) {
        super(startMark, endMark);
        this.explicit = explicit;
        this.version = version;
        this.tags = tags;
    }

    public boolean getExplicit() {
        return explicit;
    }

    public Integer[] getVersion() {
        return version;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
