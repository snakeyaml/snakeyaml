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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
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
        Object obj = createInstance(node, data);
        setProperties(obj, data.getProperties());
        return obj;
    }

    protected Object createInstance(ScalarNode node, CompactData data) {
        try {
            Class<?> clazz = getClassForName(data.getPrefix());
            Class<?>[] args = new Class[data.getArguments().size()];
            for (int i = 0; i < args.length; i++) {
                // assume all the arguments are Strings
                args[i] = String.class;
            }
            java.lang.reflect.Constructor<?> c = clazz.getDeclaredConstructor(args);
            c.setAccessible(true);
            return c.newInstance(data.getArguments().toArray());
        } catch (Exception e) {
            throw new YAMLException(e);
        }
    }

    protected void setProperties(Object bean, Map<String, String> data) {
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
}
