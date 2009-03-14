/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import org.yaml.snakeyaml.nodes.Node;

public interface Represent {
    public Node representData(Object data);
}
