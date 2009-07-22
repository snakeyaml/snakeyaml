/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.FieldProperty;
import org.yaml.snakeyaml.introspector.MethodProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * Represent JavaBeans
 */
public class Representer extends SafeRepresenter {
    private Map<Class<? extends Object>, String> classTags;

    public Representer() {
        classTags = new HashMap<Class<? extends Object>, String>();
        this.representers.put(null, new RepresentJavaBean());
    }

    /**
     * Define a tag for the <code>Class</code> to serialize
     * 
     * @param clazz
     *            <code>Class</code> which tag is changed
     * @param tag
     *            new tag to be used for every instance of the specified
     *            <code>Class</code>
     * @return the previous tag associated with the <code>Class</code>
     */
    public String addClassTag(Class<? extends Object> clazz, String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag must be provided.");
        }
        return classTags.put(clazz, tag);
    }

    private class RepresentJavaBean implements Represent {
        public Node representData(Object data) {
            Set<Property> properties;
            try {
                properties = getProperties(data.getClass());
            } catch (IntrospectionException e) {
                throw new YAMLException(e);
            }
            Node node = representJavaBean(properties, data);
            return node;
        }
    }

    /**
     * Tag logic:<br/>
     * - explicit root tag is set in serializer <br/>
     * - if there is a predefined class tag it is used<br/>
     * - a global tag with class name is always used as tag. The JavaBean parent
     * of the specified JavaBean may set another tag (tag:yaml.org,2002:map)
     * when the property class is the same as runtime class
     * 
     * @param properties
     *            JavaBean getters
     * @param javaBean
     *            instance for Node
     * @return Node to get serialized
     */
    private Node representJavaBean(Set<Property> properties, Object javaBean) {
        List<NodeTuple> value = new LinkedList<NodeTuple>();
        String tag;
        String customTag = classTags.get(javaBean.getClass());
        tag = customTag != null ? customTag : "tag:yaml.org,2002:" + javaBean.getClass().getName();
        // flow style will be chosen by BaseRepresenter
        MappingNode node = new MappingNode(tag, value, null);
        representedObjects.put(objectToRepresent, node);
        boolean bestStyle = true;
        for (Property property : properties) {
            ScalarNode nodeKey = (ScalarNode) representData(property.getName());
            Object memberValue = property.get(javaBean);
            Node nodeValue = representData(memberValue);
            if (nodeValue instanceof MappingNode) {
                // the node is a map, set or JavaBean
                if (!Map.class.isAssignableFrom(memberValue.getClass())) {
                    // the node is set or JavaBean
                    if (property.getType() == memberValue.getClass()) {
                        // we do not need global tag because the property
                        // Class is the same as the runtime class
                        if (node != nodeValue) {
                            String memberTag = "tag:yaml.org,2002:map";
                            nodeValue.setTag(memberTag);
                        } else {
                            // recursive node, keep the tag
                        }
                    }
                }
            } else if (memberValue != null && Enum.class.isAssignableFrom(memberValue.getClass())) {
                nodeValue.setTag("tag:yaml.org,2002:str");
            }
            if (nodeKey.getStyle() != null) {
                bestStyle = false;
            }
            if (!((nodeValue instanceof ScalarNode && ((ScalarNode) nodeValue).getStyle() == null))) {
                bestStyle = false;
            }
            value.add(new NodeTuple(nodeKey, nodeValue));
        }
        // recursive JavaBeans might change the tag for the parent JavaBean
        node.setTag(tag);
        if (defaultFlowStyle != null) {
            node.setFlowStyle(defaultFlowStyle);
        } else {
            node.setFlowStyle(bestStyle);
        }
        return node;
    }

    private Set<Property> getProperties(Class<? extends Object> type) throws IntrospectionException {
        Set<Property> properties = new TreeSet<Property>();
        for (PropertyDescriptor property : Introspector.getBeanInfo(type).getPropertyDescriptors())
            if (property.getReadMethod() != null
                    && !property.getReadMethod().getName().equals("getClass")) {
                properties.add(new MethodProperty(property));
            }
        // add public fields
        for (Field field : type.getFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers))
                continue;
            properties.add(new FieldProperty(field));
        }
        if (properties.isEmpty()) {
            throw new YAMLException("No JavaBean properties found in " + type.getName());
        }
        return properties;
    }

}
