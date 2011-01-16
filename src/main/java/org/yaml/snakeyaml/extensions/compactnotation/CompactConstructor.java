/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yaml.snakeyaml.extensions.compactnotation;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * Construct a custom Java instance out of a compact object notation format.
 */
public class CompactConstructor extends Constructor {
    private static final Pattern FIRST_PATTERN = Pattern.compile("(\\p{Alpha}.*)(\\s*)\\((.*?)\\)");
    private static final Pattern PROPERTY_NAME_PATTERN = Pattern
            .compile("\\s*(\\p{Alpha}\\w*)\\s*=(.+)");

    @Override
    protected Object constructScalar(ScalarNode node) {
        CompactData data = getCompactData(node.getValue());
        if (data != null) {
            return constructCompactFormat(node, data);
        } else {
            return super.constructScalar(node);
        }
    }

    protected Object constructCompactFormat(ScalarNode node, CompactData data) {
        try {
            Object obj = createInstance(node, data);
            Map<String, Object> properties = new HashMap<String, Object>(data.getProperties());
            setProperties(obj, properties);
            return obj;
        } catch (Exception e) {
            throw new YAMLException(e);
        }
    }

    protected Object createInstance(ScalarNode node, CompactData data) throws Exception {
        Class<?> clazz = getClassForName(data.getPrefix());
        Class<?>[] args = new Class[data.getArguments().size()];
        for (int i = 0; i < args.length; i++) {
            // assume all the arguments are Strings
            args[i] = String.class;
        }
        java.lang.reflect.Constructor<?> c = clazz.getDeclaredConstructor(args);
        c.setAccessible(true);
        return c.newInstance(data.getArguments().toArray());

    }

    protected void setProperties(Object bean, Map<String, Object> data) throws Exception {
        if (data == null) {
            throw new NullPointerException("Data for Compact Object Notation cannot be null.");
        }
        for (String key : data.keySet()) {
            Property property = getPropertyUtils().getProperty(bean.getClass(), key);
            try {
                property.set(bean, data.get(key));
            } catch (IllegalArgumentException e) {
                throw new YAMLException("Cannot set property='" + key + "' with value='"
                        + data.get(key) + "' (" + data.get(key).getClass() + ") in " + bean);
            }
        }
    }

    public CompactData getCompactData(String scalar) {
        if (!scalar.endsWith(")")) {
            return null;
        }
        if (scalar.indexOf('(') < 0) {
            return null;
        }
        Matcher m = FIRST_PATTERN.matcher(scalar);
        if (m.matches()) {
            String tag = m.group(1).trim();
            String content = m.group(3);
            CompactData data = new CompactData(tag);
            if (content.length() == 0)
                return data;
            String[] names = content.split("\\s*,\\s*");
            for (int i = 0; i < names.length; i++) {
                String section = names[i];
                if (section.indexOf('=') < 0) {
                    data.getArguments().add(section);
                } else {
                    Matcher sm = PROPERTY_NAME_PATTERN.matcher(section);
                    if (sm.matches()) {
                        String name = sm.group(1);
                        String value = sm.group(2).trim();
                        data.getProperties().put(name, value);
                    } else {
                        return null;
                    }
                }
            }
            return data;
        }
        return null;
    }

    @Override
    protected Construct getConstructor(Node node) {
        if (node instanceof MappingNode) {
            MappingNode mnode = (MappingNode) node;
            List<NodeTuple> list = mnode.getValue();
            if (list.size() == 1) {
                NodeTuple tuple = list.get(0);
                Node key = tuple.getKeyNode();
                if (key instanceof ScalarNode) {
                    ScalarNode scalar = (ScalarNode) key;
                    CompactData data = getCompactData(scalar.getValue());
                    if (data != null) {
                        return new ConstructCompactObject();
                    }
                }
            }
        }
        return super.getConstructor(node);
    }

    public class ConstructCompactObject extends AbstractConstruct {
        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            Map<Object, Object> map = constructMapping((MappingNode) node);
            // Compact Object Notation may contain only one entry
            Map.Entry<Object, Object> entry = map.entrySet().iterator().next();
            Object result = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> properties = (Map<String, Object>) value;
                try {
                    setProperties(result, properties);
                } catch (Exception e) {
                    throw new YAMLException(e);
                }
            } else {
                // value is a list
                applySequence(result, (List<?>) value);
            }
            return result;
        }
    }

    protected void applySequence(Object bean, List<?> value) {
        try {
            Property property = getPropertyUtils().getProperty(bean.getClass(),
                    getSequencePropertyName(bean.getClass()));
            property.set(bean, value);
        } catch (Exception e) {
            throw new YAMLException(e);
        }
    }

    /**
     * Provide the name of the property which is used when the entries form a
     * sequence. The property must be a List.
     * 
     * @throws IntrospectionException
     */
    protected String getSequencePropertyName(Class<?> bean) throws IntrospectionException {
        Set<Property> properties = getPropertyUtils().getProperties(bean);
        for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext();) {
            Property property = iterator.next();
            if (!List.class.isAssignableFrom(property.getType())) {
                iterator.remove();
            }
        }
        if (properties.size() == 0) {
            throw new YAMLException("No list property found in " + bean);
        } else if (properties.size() > 1) {
            throw new YAMLException(
                    "Many list properties found in "
                            + bean
                            + "; Please override getSequencePropertyName() to specify which property to use.");
        }
        return properties.iterator().next().getName();
    }
}
