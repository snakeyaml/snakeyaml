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

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.FieldProperty;
import org.yaml.snakeyaml.introspector.MethodProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * Represent JavaBeans
 */
public class Representer extends SafeRepresenter {
    private Map<Class<? extends Object>, String> classTags;
    private Map<Class<? extends Object>, TypeDescription> classDefinitions;
    private String rootTag = null;

    public Representer() {
        classTags = new HashMap<Class<? extends Object>, String>();
        classDefinitions = new HashMap<Class<? extends Object>, TypeDescription>();
        this.representers.put(null, new RepresentJavaBean());
    }

    /**
     * Make YAML aware how to represent a custom Class. If there is no root
     * Class assigned in constructor then the 'root' property of this definition
     * is respected.
     * 
     * @param definition
     *            to be added to the Constructor
     * @return the previous value associated with <tt>definition</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>definition</tt>.
     */
    public TypeDescription addTypeDescription(TypeDescription definition) {
        if (definition == null) {
            throw new NullPointerException("ClassDescription is required.");
        }
        String tag = definition.getTag();
        classTags.put(definition.getType(), tag);
        return classDefinitions.put(definition.getType(), definition);
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

    private Node representJavaBean(Set<Property> properties, Object javaBean) {
        List<Node[]> value = new LinkedList<Node[]>();
        String tag;
        String customTag = classTags.get(javaBean.getClass());
        if (customTag == null) {
            if (rootTag == null) {
                tag = "tag:yaml.org,2002:" + javaBean.getClass().getName();
            } else {
                tag = "tag:yaml.org,2002:map";
            }
        } else {
            tag = customTag;
        }
        if (rootTag == null && isRoot) {
            rootTag = tag;
        }
        // flow style will be chosen by BaseRepresenter
        MappingNode node = new MappingNode(tag, value, null);
        representedObjects.put(aliasKey, node);
        boolean bestStyle = true;
        for (Property property : properties) {
            ScalarNode nodeKey = (ScalarNode) representData(property.getName());
            Object memberValue = property.get(javaBean);
            Node nodeValue = representData(memberValue);
            if (nodeValue instanceof MappingNode) {
                if (!Map.class.isAssignableFrom(memberValue.getClass())) {
                    if (property.getType() != memberValue.getClass()) {
                        String memberTag = "tag:yaml.org,2002:" + memberValue.getClass().getName();
                        nodeValue.setTag(memberTag);
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
            value.add(new Node[] { nodeKey, nodeValue });
        }
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
