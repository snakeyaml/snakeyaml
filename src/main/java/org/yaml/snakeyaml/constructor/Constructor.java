/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.FieldProperty;
import org.yaml.snakeyaml.introspector.MethodProperty;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Constructor extends SafeConstructor {
    private final Map<String, Class<? extends Object>> typeTags;
    private final Map<Class<? extends Object>, TypeDescription> typeDefinitions;

    public Constructor() {
        this(Object.class);
    }

    public Constructor(Class<? extends Object> theRoot) {
        if (theRoot == null) {
            throw new NullPointerException("Root type must be provided.");
        }
        this.yamlConstructors.put(null, new ConstructYamlObject());
        rootType = theRoot;
        typeTags = new HashMap<String, Class<? extends Object>>();
        typeDefinitions = new HashMap<Class<? extends Object>, TypeDescription>();
    }

    /**
     * Create Constructor for a class which does not have to be in the classpath
     * or for a definition from a Spring ApplicationContext.
     * 
     * @param theRoot
     *            fully qualified class name of the root JavaBean
     * @throws ClassNotFoundException
     */
    public Constructor(String theRoot) throws ClassNotFoundException {
        this(Class.forName(check(theRoot)));
    }

    private static final String check(String s) {
        if (s == null) {
            throw new NullPointerException("Root type must be provided.");
        }
        if (s.trim().length() == 0) {
            throw new YAMLException("Root type must be provided.");
        }
        return s;
    }

    /**
     * Make YAML aware how to parse a custom Class. If there is no root Class
     * assigned in constructor then the 'root' property of this definition is
     * respected.
     * 
     * @param definition
     *            to be added to the Constructor
     * @return the previous value associated with <tt>definition</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>definition</tt>.
     */
    public TypeDescription addTypeDescription(TypeDescription definition) {
        if (definition == null) {
            throw new NullPointerException("TypeDescription is required.");
        }
        if (rootType == Object.class && definition.isRoot()) {
            rootType = definition.getType();
        }
        String tag = definition.getTag();
        typeTags.put(tag, definition.getType());
        return typeDefinitions.put(definition.getType(), definition);
    }

    private class ConstructYamlObject extends AbstractConstruct {
        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            Object result = null;
            try {
                Class cl = getClassForNode(node);
                java.lang.reflect.Constructor javaConstructor;
                switch (node.getNodeId()) {
                case mapping:
                    node.setType(cl);
                    if (node.isTwoStepsConstruction()) {
                        result = createJavaBeanNode(node);
                    } else {
                        result = constructJavaBeanNode((MappingNode) node);
                    }
                    break;
                case sequence:
                    SequenceNode seqNode = (SequenceNode) node;
                    List<Object> values = (List<Object>) constructSequence(seqNode);
                    Class[] parameterTypes = new Class[values.size()];
                    int index = 0;
                    for (Object parameter : values) {
                        parameterTypes[index] = parameter.getClass();
                        index++;
                    }
                    javaConstructor = cl.getConstructor(parameterTypes);
                    Object[] initargs = values.toArray();
                    result = javaConstructor.newInstance(initargs);
                    break;
                default:// scalar
                    ScalarNode scaNode = (ScalarNode) node;
                    Object value = constructScalar(scaNode);
                    if (Enum.class.isAssignableFrom(cl)) {
                        String enumValueName = (String) node.getValue();
                        try {
                            result = Enum.valueOf(cl, enumValueName);
                        } catch (Exception ex) {
                            throw new YAMLException("Unable to find enum value '" + enumValueName
                                    + "' for enum class: " + cl.getName());
                        }
                    } else {
                        javaConstructor = cl.getConstructor(value.getClass());
                        result = javaConstructor.newInstance(value);
                    }
                }
            } catch (Exception e) {
                throw new ConstructorException(null, null, "Can't construct a java object for "
                        + node.getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
            }
            return result;
        }
    }

    @Override
    protected Object callConstructor(Node node) {
        if (Object.class.equals(node.getType()) || "tag:yaml.org,2002:null".equals(node.getTag())) {
            return super.callConstructor(node);
        }
        Object result;
        switch (node.getNodeId()) {
        case scalar:
            result = constructJavaBeanScalarNode((ScalarNode) node);
            break;
        case sequence:
            SequenceNode snode = (SequenceNode) node;
            if (node.isTwoStepsConstruction()) {
                result = createDefaultList(snode.getValue().size());
            } else {
                result = constructSequence(snode);
            }
            break;
        default:// mapping
            if (Map.class.isAssignableFrom(node.getType())) {
                if (node.isTwoStepsConstruction()) {
                    result = createDefaultMap();
                } else {
                    result = constructMapping((MappingNode) node);
                }
            } else if (Set.class.isAssignableFrom(node.getType())) {
                if (node.isTwoStepsConstruction()) {
                    result = createDefaultSet();
                } else {
                    result = constructSet((MappingNode) node);
                }
            } else {
                if (node.isTwoStepsConstruction()) {
                    result = createJavaBeanNode(node);
                } else {
                    result = constructJavaBeanNode((MappingNode) node);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void callPostCreate(Node node, Object object) {
        if (!node.isTwoStepsConstruction()) {
            throw new YAMLException("Inexpected recursive structure. Node: " + node);
        }
        if (Object.class.equals(node.getType()) || "tag:yaml.org,2002:null".equals(node.getTag())) {
            super.callPostCreate(node, object);
        } else {
            switch (node.getNodeId()) {
            case scalar:
                throw new YAMLException("Scalars cannot be recursive. Node: " + node);
            case sequence:
                constructSequenceStep2((SequenceNode) node, (List<Object>) object);
                break;
            default:// mapping
                if (Map.class.isAssignableFrom(node.getType())) {
                    constructMapping2ndStep((MappingNode) node, (Map<Object, Object>) object);
                } else if (Set.class.isAssignableFrom(node.getType())) {
                    constructSet2ndStep((MappingNode) node, (Set<Object>) object);
                } else {
                    constructJavaBeanNode2ndStep((MappingNode) node, object);
                }
            }
        }
    }

    private Object constructJavaBeanScalarNode(ScalarNode node) {
        Class<? extends Object> type = node.getType();
        Object result;
        if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type)
                || type == Boolean.class || Date.class.isAssignableFrom(type)
                || type == Character.class || type == BigInteger.class
                || Enum.class.isAssignableFrom(type)) {
            if (type == String.class) {
                Construct stringConstructor = yamlConstructors.get("tag:yaml.org,2002:str");
                result = stringConstructor.construct((ScalarNode) node);
            } else if (type == Boolean.class || type == Boolean.TYPE) {
                Construct boolConstructor = yamlConstructors.get("tag:yaml.org,2002:bool");
                result = boolConstructor.construct((ScalarNode) node);
            } else if (type == Character.class || type == Character.TYPE) {
                Construct charConstructor = yamlConstructors.get("tag:yaml.org,2002:str");
                String ch = (String) charConstructor.construct((ScalarNode) node);
                if (ch.length() != 1) {
                    throw new YAMLException("Invalid node Character: '" + ch + "'; length: "
                            + ch.length());
                }
                result = new Character(ch.charAt(0));
            } else if (Date.class.isAssignableFrom(type)) {
                Construct dateConstructor = yamlConstructors.get("tag:yaml.org,2002:timestamp");
                Date date = (Date) dateConstructor.construct((ScalarNode) node);
                if (type == Date.class) {
                    result = date;
                } else {
                    try {
                        java.lang.reflect.Constructor<?> constr = type.getConstructor(long.class);
                        result = constr.newInstance(date.getTime());
                    } catch (Exception e) {
                        throw new YAMLException("Cannot construct: '" + type + "'");
                    }
                }
            } else if (type == Float.class || type == Double.class || type == Float.TYPE
                    || type == Double.TYPE || type == BigDecimal.class) {
                Construct doubleConstructor = yamlConstructors.get("tag:yaml.org,2002:float");
                result = doubleConstructor.construct(node);
                if (type == Float.class || type == Float.TYPE) {
                    result = new Float((Double) result);
                } else if (type == BigDecimal.class) {
                    result = new BigDecimal(((Double) result).doubleValue());
                }
            } else if (type == Byte.class || type == Short.class || type == Integer.class
                    || type == Long.class || type == BigInteger.class || type == Byte.TYPE
                    || type == Short.TYPE || type == Integer.TYPE || type == Long.TYPE) {
                Construct intConstructor = yamlConstructors.get("tag:yaml.org,2002:int");
                result = intConstructor.construct(node);
                if (type == Byte.class || type == Byte.TYPE) {
                    result = new Byte(result.toString());
                } else if (type == Short.class || type == Short.TYPE) {
                    result = new Short(result.toString());
                } else if (type == Integer.class || type == Integer.TYPE) {
                    result = new Integer(result.toString());
                } else if (type == Long.class || type == Long.TYPE) {
                    result = new Long(result.toString());
                } else {
                    // only BigInteger left
                    result = new BigInteger(result.toString());
                }
            } else if (Enum.class.isAssignableFrom(type)) {
                String tag = "tag:yaml.org,2002:" + type.getName();
                node.setTag(tag);
                result = super.callConstructor(node);
            } else {
                throw new YAMLException("Unsupported class: " + type);
            }
        } else {
            try {
                // get value by BaseConstructor
                Object value = super.callConstructor(node);
                if (type.isArray()) {
                    result = value;
                } else {
                    java.lang.reflect.Constructor<? extends Object> javaConstructor = type
                            .getConstructor(value.getClass());
                    result = javaConstructor.newInstance(value);
                }
            } catch (Exception e) {
                throw new YAMLException(e);
            }
        }
        return result;
    }

    private Object createJavaBeanNode(Node node) {
        try {
            Class<? extends Object> type = node.getType();
            if (Modifier.isAbstract(type.getModifiers())) {
                node.setType(getClassForNode(node));
            }
            /**
             * Using only default constructor. Everything else will be
             * initialized on 2nd step. If we do here some partial
             * initialization, how do we then track what need to be done on 2nd
             * step? I think it is better to get only object here (to have it as
             * reference for recursion) and do all other thing on 2nd step.
             */
            return node.getType().newInstance();
        } catch (InstantiationException e) {
            throw new YAMLException(e);
        } catch (IllegalAccessException e) {
            throw new YAMLException(e);
        } catch (ClassNotFoundException e) {
            throw new YAMLException(e);
        }
    }

    /**
     * Construct JavaBean. If type safe collections are used please look at
     * <code>TypeDescription</code>.
     * 
     * @param node
     *            node where the keys are property names (they can only be
     *            <code>String</code>s) and values are objects to be created
     * @return constructed JavaBean
     */
    private Object constructJavaBeanNode(MappingNode node) {
        return constructJavaBeanNode2ndStep(node, createJavaBeanNode(node));
    }

    @SuppressWarnings("unchecked")
    private Object constructJavaBeanNode2ndStep(MappingNode node, Object object) {
        Class<? extends Object> beanType = node.getType();
        List<NodeTuple> nodeValue = (List<NodeTuple>) node.getValue();
        for (NodeTuple tuple : nodeValue) {
            ScalarNode keyNode;
            if (tuple.getKeyNode() instanceof ScalarNode) {
                // key must be scalar
                keyNode = (ScalarNode) tuple.getKeyNode();
            } else {
                throw new YAMLException("Keys must be scalars but found: " + tuple.getKeyNode());
            }
            Node valueNode = tuple.getValueNode();
            // keys can only be Strings
            keyNode.setType(String.class);
            String key = (String) constructObject(keyNode);
            boolean isArray = false;
            try {
                Property property = getProperty(beanType, key);
                valueNode.setType(property.getType());
                TypeDescription memberDescription = typeDefinitions.get(beanType);
                if (memberDescription != null) {
                    switch (valueNode.getNodeId()) {
                    case sequence:
                        SequenceNode snode = (SequenceNode) valueNode;
                        Class<? extends Object> memberType = memberDescription
                                .getListPropertyType(key);
                        if (memberType != null) {
                            snode.setListType(memberType);
                        } else if (property.getType().isArray()) {
                            isArray = true;
                            snode.setListType(property.getType().getComponentType());
                        }
                        break;
                    case mapping:
                        MappingNode mnode = (MappingNode) valueNode;
                        Class<? extends Object> keyType = memberDescription.getMapKeyType(key);
                        if (keyType != null) {
                            mnode.setKeyType(keyType);
                            mnode.setValueType(memberDescription.getMapValueType(key));
                        }
                        break;
                    }
                }
                Object value = constructObject(valueNode);
                if (isArray) {
                    List<Object> list = (List<Object>) value;
                    value = list.toArray(createArray(property.getType()));
                }
                property.set(object, value);
            } catch (Exception e) {
                throw new YAMLException(e);
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private <T> T[] createArray(Class<T> type) {
        return (T[]) Array.newInstance(type.getComponentType(), 0);
    }

    private Property getProperty(Class<? extends Object> type, String name)
            throws IntrospectionException {
        for (PropertyDescriptor property : Introspector.getBeanInfo(type).getPropertyDescriptors()) {
            if (property.getName().equals(name)) {
                if (property.getWriteMethod() != null) {
                    return new MethodProperty(property);
                } else {
                    throw new YAMLException("Property '" + name + "' on JavaBean: "
                            + type.getName() + " does not have the write method");
                }
            }
        }
        for (Field field : type.getFields()) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
                continue;
            }
            if (field.getName().equals(name)) {
                return new FieldProperty(field);
            }
        }
        throw new YAMLException("Unable to find property '" + name + "' on class: "
                + type.getName());
    }

    protected Class<?> getClassForNode(Node node) throws ClassNotFoundException {
        Class<? extends Object> customTag = typeTags.get(node.getTag());
        if (customTag == null) {
            if (node.getTag().length() < "tag:yaml.org,2002:".length()) {
                throw new YAMLException("Unknown tag: " + node.getTag());
            }
            String name = node.getTag().substring("tag:yaml.org,2002:".length());
            Class<?> cl = Class.forName(name);
            typeTags.put(node.getTag(), cl);
            return cl;
        } else {
            return customTag;
        }
    }

}
