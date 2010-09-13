/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            property.set(bean, data.get(key));
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
            if (map.size() != 1) {
                throw new YAMLException("Compact Object Notation may contain only one entry: "
                        + map.size());
            }
            Map.Entry<Object, Object> entry = map.entrySet().iterator().next();
            Object result = entry.getKey();
            Map<String, Object> properties = (Map<String, Object>) entry.getValue();
            try {
                setProperties(result, properties);
            } catch (Exception e) {
                throw new YAMLException(e);
            }
            return result;
        }
    }
}
