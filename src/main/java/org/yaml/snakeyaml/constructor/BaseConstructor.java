/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public abstract class BaseConstructor {
    protected final Map<String, Construct> yamlConstructors = new HashMap<String, Construct>();

    private Composer composer;
    private final Map<Node, Object> constructedObjects;
    private final Set<Node> recursiveObjects;
    private final Stack<Tuple<Node, Object>> toBeConstructedAt2ndStep;
    private final LinkedList<Tuple<Map<Object, Object>, Tuple<Object, Object>>> maps2fill;
    private final LinkedList<Tuple<Set<Object>, Object>> sets2fill;

    protected Class<? extends Object> rootType;

    public BaseConstructor() {
        constructedObjects = new HashMap<Node, Object>();
        recursiveObjects = new HashSet<Node>();
        toBeConstructedAt2ndStep = new Stack<Tuple<Node, Object>>();
        maps2fill = new LinkedList<Tuple<Map<Object, Object>, Tuple<Object, Object>>>();
        sets2fill = new LinkedList<Tuple<Set<Object>, Object>>();
        rootType = Object.class;
        // TODO clear collections
    }

    public void setComposer(Composer composer) {
        this.composer = composer;
    }

    public boolean checkData() {
        // If there are more documents available?
        return composer.checkNode();
    }

    public Object getData() {
        // Construct and return the next document.
        composer.checkNode();
        Node node = composer.getNode();
        node.setType(rootType);
        return constructDocument(node);
    }

    public Object getSingleData() {
        // Ensure that the stream contains a single document and construct it
        Node node = composer.getSingleNode();
        if (node != null) {
            node.setType(rootType);
            return constructDocument(node);
        }
        return null;
    }

    private Object constructDocument(Node node) {
        Object data = constructObject(node);
        while (!toBeConstructedAt2ndStep.isEmpty()) {
            Tuple<Node, Object> toBeProcessed = toBeConstructedAt2ndStep.pop();
            callPostCreate(toBeProcessed._1(), toBeProcessed._2());
        }
        if (!maps2fill.isEmpty()) {
            for (Tuple<Map<Object, Object>, Tuple<Object, Object>> entry : maps2fill) {
                Tuple<Object, Object> key_value = entry._2();
                entry._1().put(key_value._1(), key_value._2());
            }
            maps2fill.clear();
        }
        if (!sets2fill.isEmpty()) {
            for (Tuple<Set<Object>, Object> value : sets2fill) {
                value._1().add(value._2());
            }
            sets2fill.clear();
        }
        constructedObjects.clear();
        recursiveObjects.clear();
        toBeConstructedAt2ndStep.clear();
        return data;
    }

    protected Object constructObject(Node node) {
        if (constructedObjects.containsKey(node)) {
            return constructedObjects.get(node);
        }
        if (recursiveObjects.contains(node)) {
            throw new ConstructorException(null, null, "found unconstructable recursive node", node
                    .getStartMark());
        }
        recursiveObjects.add(node);
        Object data = callConstructor(node);
        if (node.isTwoStepsConstruction()) {
            toBeConstructedAt2ndStep.push(new Tuple<Node, Object>(node, data));
        }
        constructedObjects.put(node, data);
        recursiveObjects.remove(node);
        return data;
    }

    protected Object callConstructor(Node node) {
        return getConstructor(node).construct(node);
    }

    protected void callPostCreate(Node node, Object object) {
        getConstructor(node).construct2ndStep(node, object);
    }

    private Construct getConstructor(Node node) {
        Construct constructor = yamlConstructors.get(node.getTag());
        if (constructor == null) {
            return yamlConstructors.get(null);
        }
        return constructor;
    }

    protected Object constructScalar(ScalarNode node) {
        return node.getValue();
    }

    protected List<Object> createDefaultList(int initSize) {
        return new LinkedList<Object>();
    }

    protected List<? extends Object> constructSequence(SequenceNode node) {
        List<Object> result = createDefaultList(node.getValue().size());
        constructSequenceStep2(node, result);
        return result;
    }

    protected void constructSequenceStep2(SequenceNode node, List<Object> list) {
        for (Node child : node.getValue()) {
            list.add(constructObject(child));
        }
    }

    protected Map<Object, Object> createDefaultMap() {
        // respect order from YAML document
        return new LinkedHashMap<Object, Object>();
    }

    protected Set<Object> createDefaultSet() {
        // respect order from YAML document
        return new LinkedHashSet<Object>();
    }

    protected Set<Object> constructSet(MappingNode node) {
        Set<Object> set = createDefaultSet();
        constructSet2ndStep(node, set);
        return set;
    }

    protected Map<Object, Object> constructMapping(MappingNode node) {
        Map<Object, Object> mapping = createDefaultMap();
        constructMapping2ndStep(node, mapping);
        return mapping;
    }

    protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
        List<Node[]> nodeValue = (List<Node[]>) node.getValue();
        for (Node[] tuple : nodeValue) {
            Node keyNode = tuple[0];
            Node valueNode = tuple[1];
            Object key = constructObject(keyNode);
            if (key != null) {
                try {
                    key.hashCode();// check circular dependencies
                } catch (Exception e) {
                    throw new ConstructorException("while constructing a mapping", node
                            .getStartMark(), "found unacceptable key " + key, tuple[0]
                            .getStartMark(), e);
                }
            }
            Object value = constructObject(valueNode);
            if (keyNode.isTwoStepsConstruction()) {
                /*
                 * if keyObject is created it 2 steps we should postpone putting
                 * it in map because it may have different hash after
                 * initialization compared to clean just created one. And map of
                 * course does not observe key hashCode changes.
                 */
                maps2fill.addFirst(new Tuple<Map<Object, Object>, Tuple<Object, Object>>(mapping,
                        new Tuple<Object, Object>(key, value)));
            } else {
                mapping.put(key, value);
            }
        }
    }

    protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
        List<Node[]> nodeValue = (List<Node[]>) node.getValue();
        for (Node[] tuple : nodeValue) {
            Node keyNode = tuple[0];
            Object key = constructObject(keyNode);
            if (key != null) {
                try {
                    key.hashCode();// check circular dependencies
                } catch (Exception e) {
                    throw new ConstructorException("while constructing a Set", node.getStartMark(),
                            "found unacceptable key " + key, tuple[0].getStartMark(), e);
                }
            }
            if (keyNode.isTwoStepsConstruction()) {
                /*
                 * if keyObject is created it 2 steps we should postpone putting
                 * it into the set because it may have different hash after
                 * initialization compared to clean just created one. And set of
                 * course does not observe value hashCode changes.
                 */
                sets2fill.addFirst(new Tuple<Set<Object>, Object>(set, key));
            } else {
                set.add(key);
            }
        }
    }

    // TODO protected List<Object[]> constructPairs(MappingNode node) {
    // List<Object[]> pairs = new LinkedList<Object[]>();
    // List<Node[]> nodeValue = (List<Node[]>) node.getValue();
    // for (Iterator<Node[]> iter = nodeValue.iterator(); iter.hasNext();) {
    // Node[] tuple = iter.next();
    // Object key = constructObject(Object.class, tuple[0]);
    // Object value = constructObject(Object.class, tuple[1]);
    // pairs.add(new Object[] { key, value });
    // }
    // return pairs;
    // }
}
