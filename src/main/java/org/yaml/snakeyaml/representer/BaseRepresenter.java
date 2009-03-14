/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.serializer.Serializer;

public abstract class BaseRepresenter {
    @SuppressWarnings("unchecked")
    protected final Map<Class, Represent> representers = new HashMap<Class, Represent>();
    /**
     * in Java 'null' is not a type. So we have to keep the null representer
     * separately otherwise it will coincide with the default representer which
     * is stored with the key null.
     */
    protected Represent nullRepresenter;
    @SuppressWarnings("unchecked")
    protected final Map<Class, Represent> multiRepresenters = new HashMap<Class, Represent>();
    private Character defaultStyle;
    protected Boolean defaultFlowStyle;
    protected final Map<Integer, Node> representedObjects = new HashMap<Integer, Node>();
    private final Set<Object> objectKeeper = new HashSet<Object>();
    protected Integer aliasKey;// internal memory address
    protected String rootTag = null;

    public BaseRepresenter(Character default_style, Boolean default_flow_style) {
        this.defaultStyle = default_style;
        this.defaultFlowStyle = default_flow_style;
    }

    public void represent(Serializer serializer, Object data) throws IOException {
        Node node = representData(data);
        serializer.serialize(node);
        representedObjects.clear();
        objectKeeper.clear();
    }

    @SuppressWarnings("unchecked")
    protected Node representData(Object data) {
        aliasKey = System.identityHashCode(data);// take memory address
        if (!ignoreAliases(data)) {
            // check for identity
            if (representedObjects.containsKey(aliasKey)) {
                Node node = representedObjects.get(aliasKey);
                return node;
            }
        }
        // check for null first
        if (data == null) {
            Node node = nullRepresenter.representData(data);
            return node;
        }
        // check the same class
        Node node;
        Class clazz = data.getClass();
        if (representers.containsKey(clazz)) {
            Represent representer = representers.get(clazz);
            node = representer.representData(data);
        } else {
            // check the parents
            for (Class repr : multiRepresenters.keySet()) {
                if (repr.isInstance(data)) {
                    Represent representer = multiRepresenters.get(repr);
                    node = representer.representData(data);
                    return node;
                }
            }
            // check array of primitives
            if (clazz.isArray()) {
                throw new YAMLException("Arrays of primitives are not fully supported.");
            }
            // check defaults
            if (multiRepresenters.containsKey(null)) {
                Represent representer = multiRepresenters.get(null);
                node = representer.representData(data);
            } else {
                Represent representer = representers.get(null);
                node = representer.representData(data);
            }
        }
        return node;
    }

    protected Node representScalar(String tag, String value, Character style) {
        if (style == null) {
            style = this.defaultStyle;
        }
        Node node = new ScalarNode(tag, value, null, null, style);
        representedObjects.put(aliasKey, node);
        return node;
    }

    protected Node representScalar(String tag, String value) {
        return representScalar(tag, value, null);
    }

    protected Node representSequence(String tag, List<? extends Object> sequence, Boolean flowStyle) {
        List<Node> value = new LinkedList<Node>();
        SequenceNode node = new SequenceNode(tag, value, flowStyle);
        representedObjects.put(aliasKey, node);
        boolean bestStyle = true;
        for (Object item : sequence) {
            Node nodeItem = representData(item);
            if (!((nodeItem instanceof ScalarNode && ((ScalarNode) nodeItem).getStyle() == null))) {
                bestStyle = false;
            }
            value.add(nodeItem);
        }
        if (flowStyle == null) {
            if (defaultFlowStyle != null) {
                node.setFlowStyle(defaultFlowStyle);
            } else {
                node.setFlowStyle(bestStyle);
            }
        }
        return node;
    }

    protected Node representMapping(String tag, Map<? extends Object, Object> mapping,
            Boolean flowStyle) {
        List<Node[]> value = new LinkedList<Node[]>();
        MappingNode node = new MappingNode(tag, value, flowStyle);
        representedObjects.put(aliasKey, node);
        boolean bestStyle = true;
        for (Object itemKey : mapping.keySet()) {
            Object itemValue = mapping.get(itemKey);
            Node nodeKey = representData(itemKey);
            Node nodeValue = representData(itemValue);
            if (!((nodeKey instanceof ScalarNode && ((ScalarNode) nodeKey).getStyle() == null))) {
                bestStyle = false;
            }
            if (!((nodeValue instanceof ScalarNode && ((ScalarNode) nodeValue).getStyle() == null))) {
                bestStyle = false;
            }
            value.add(new Node[] { nodeKey, nodeValue });
        }
        if (flowStyle == null) {
            if (defaultFlowStyle != null) {
                node.setFlowStyle(defaultFlowStyle);
            } else {
                node.setFlowStyle(bestStyle);
            }
        }
        return node;
    }

    protected abstract boolean ignoreAliases(Object data);
}
