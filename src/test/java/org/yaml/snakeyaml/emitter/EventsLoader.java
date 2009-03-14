/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.emitter;

import org.yaml.snakeyaml.Loader;

public class EventsLoader extends Loader {

    public EventsLoader() {
        super(new EventConstructor());
    }
}
