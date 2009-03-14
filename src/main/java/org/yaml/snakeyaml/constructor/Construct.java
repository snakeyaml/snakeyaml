/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;

public interface Construct {
    public Object construct(Node node);
}
