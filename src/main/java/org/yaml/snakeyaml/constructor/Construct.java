/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.Node;

/**
 * Provide a way to construct a Java instance out of the composed Node. Support
 * recursive objects if it is required. (create Native Data Structure out of
 * Node Graph)
 * 
 * @see http://yaml.org/spec/1.1/#id859109
 */
public interface Construct {
    /**
     * Construct a Java instance with all the properties injected when it is
     * possible.
     * 
     * @param node
     *            composed Node
     * @return a complete Java instance
     */
    public Object construct(Node node);

    /**
     * Apply the second step when constructing recursive structures. Because the
     * instance is already created it can assign a reference to itself.
     * 
     * @param node
     *            composed Node
     * @param object
     *            the instance constructed earlier by
     *            <code>construct(Node node)</code> for the provided Node
     */
    public void construct2ndStep(Node node, Object object);
}
