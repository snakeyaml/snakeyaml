/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.events.Event;

interface Production {
    public Event produce();
}
