/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import org.yaml.snakeyaml.nodes.Node;

/**
 * Create a Node Graph out of the provided Native Data Structure (Java
 * instance).
 * 
 * @see http://yaml.org/spec/1.1/#id859109
 */
public interface Represent {
    /**
     * Create a Node
     * 
     * @param data
     *            the instance to represent
     * @return Node to dump
     */
    public Node representData(Object data);
}
