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
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Representer extends SafeRepresenter {
    private Map<Class<? extends Object>, String> classTags;
    private Map<Class<? extends Object>, TypeDescription> classDefinitions;

    public Representer(Character default_style, Boolean default_flow_style) {
        super(default_style, default_flow_style);
        classTags = new HashMap<Class<? extends Object>, String>();
        classDefinitions = new HashMap<Class<? extends Object>, TypeDescription>();
        this.representers.put(null, new RepresentJavaBean());
    }

    public Representer() {
        this(null, null);
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
            Node node = representMapping(properties, data);
            return node;
        }
    }

    private Node representMapping(Set<Property> properties, Object javaBean) {
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
        if (rootTag == null) {
            rootTag = tag;
        }
        // flow style will be chosen by BaseRepresenter
        MappingNode node = new MappingNode(tag, value, null);
        representedObjects.put(aliasKey, node);
        boolean bestStyle = true;
        for (Property property : properties) {
            Node nodeKey = representData(property.getName());
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
            if (!((nodeKey instanceof ScalarNode && ((ScalarNode) nodeKey).getStyle() == null))) {
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
        for (Field field : type.getFields()) {
            int modifiers = field.getModifiers();
            if (!Modifier.isPublic(modifiers) || Modifier.isStatic(modifiers)
                    || Modifier.isTransient(modifiers))
                continue;
            properties.add(new FieldProperty(field));
        }
        return properties;
    }
}
