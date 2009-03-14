/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class StreamStartEvent extends Event {

    public StreamStartEvent(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }
}
