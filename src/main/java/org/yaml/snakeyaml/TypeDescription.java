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

package org.yaml.snakeyaml;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.ArtificialProperty;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Provides additional runtime information necessary to create a custom Java
 * instance.
 */
public class TypeDescription {

    private final Class<? extends Object> type;
    private Class<?> impl;

    private Tag tag;
    private boolean root;

    transient private Set<Property> dumpProperties = null;
    transient private PropertyUtils propertyUtils;
    transient private boolean delegatesChecked = false;

    private Map<String, ArtificialProperty> properties = Collections.emptyMap();

    protected Set<String> excludes = Collections.emptySet();
    protected String[] includes = null;
    protected BeanAccess beanAccess;

    public TypeDescription(Class<? extends Object> clazz, Tag tag) {
        this(clazz, tag, null);
    }

    public TypeDescription(Class<? extends Object> clazz, Tag tag, Class<?> impl) {
        this.type = clazz;
        this.tag = tag;
        this.impl = impl;
        beanAccess = null;
    }

    public TypeDescription(Class<? extends Object> clazz, String tag) {
        this(clazz, new Tag(tag), null);
    }

    public TypeDescription(Class<? extends Object> clazz) {
        this(clazz, (Tag) null, null);
    }

    public TypeDescription(Class<? extends Object> clazz, Class<?> impl) {
        this(clazz, null, impl);
    }

    /**
     * Get tag which shall be used to load or dump the type (class).
     * 
     * @return tag to be used. It may be a tag for Language-Independent Types
     *         (http://www.yaml.org/type/)
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Set tag to be used to load or dump the type (class).
     * 
     * @param tag
     *            local or global tag
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        setTag(new Tag(tag));
    }

    /**
     * Get represented type (class)
     * 
     * @return type (class) to be described.
     */
    public Class<? extends Object> getType() {
        return type;
    }

    /**
     * Defines whether this type (class) is the root of the YAML document
     * 
     * @return true if this type shall be used as a root of object hierarchy.
     */
    public boolean isRoot() {
        return root;
    }

    /**
     * Specify whether this type (class) should be serve as the root of the YAML
     * document
     * 
     * @param root
     *            true if this type shall be used as a root of object hierarchy.
     */
    public void setRoot(boolean root) {
        this.root = root;
    }

    /**
     * Specify that the property is a type-safe <code>List</code>.
     * 
     * @param property
     *            name of the JavaBean property
     * @param type
     *            class of List values
     */
    @Deprecated
    public void putListPropertyType(String property, Class<? extends Object> type) {
        if (properties.containsKey(property)) {
            ArtificialProperty pr = properties.get(property);
            pr.setActualTypeArguments(type);
        } else {
            addPropertyMock(property, null, null, null, type);
        }
    }

    /**
     * Get class of List values for provided JavaBean property.
     * 
     * @param property
     *            property name
     * @return class of List values
     */
    @Deprecated
    public Class<? extends Object> getListPropertyType(String property) {
        if (properties.containsKey(property)) {
            Class<?>[] typeArguments = properties.get(property).getActualTypeArguments();
            if (typeArguments != null && typeArguments.length > 0) {
                return typeArguments[0];
            }
        }
        return null;
    }

    /**
     * Specify that the property is a type-safe <code>Map</code>.
     * 
     * @param property
     *            property name of this JavaBean
     * @param key
     *            class of keys in Map
     * @param value
     *            class of values in Map
     */
    @Deprecated
    public void putMapPropertyType(String property, Class<? extends Object> key,
            Class<? extends Object> value) {
        if (properties.containsKey(property)) {
            ArtificialProperty pr = properties.get(property);
            pr.setActualTypeArguments(key, value);
        } else {
            addPropertyMock(property, null, null, null, key, value);
        }
    }

    /**
     * Get keys type info for this JavaBean
     * 
     * @param property
     *            property name of this JavaBean
     * @return class of keys in the Map
     */
    @Deprecated
    public Class<? extends Object> getMapKeyType(String property) {
        if (properties.containsKey(property)) {
            Class<?>[] typeArguments = properties.get(property).getActualTypeArguments();
            if (typeArguments != null && typeArguments.length > 0) {
                return typeArguments[0];
            }
        }
        return null;
    }

    /**
     * Get values type info for this JavaBean
     * 
     * @param property
     *            property name of this JavaBean
     * @return class of values in the Map
     */
    @Deprecated
    public Class<? extends Object> getMapValueType(String property) {
        if (properties.containsKey(property)) {
            Class<?>[] typeArguments = properties.get(property).getActualTypeArguments();
            if (typeArguments != null && typeArguments.length > 1) {
                return typeArguments[1];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
    }

    private void checkDelegates() {
        Collection<ArtificialProperty> values = properties.values();
        for (ArtificialProperty p : values) {
            try {
                p.setDelegate(discoverProperty(p.getName()));
            } catch (YAMLException e) {
            }
        }
        delegatesChecked = true;
    }

    private Property discoverProperty(String name) {
        if (propertyUtils != null) {
            if (beanAccess == null) {
                return propertyUtils.getProperty(type, name);
            }
            return propertyUtils.getProperty(type, name, beanAccess);
        }
        return null;
    }

    public Property getProperty(String name) {
        if (!delegatesChecked) {
            checkDelegates();
        }
        return properties.containsKey(name) ? properties.get(name) : discoverProperty(name);
    }

    public void addPropertyMock(String pName, Class<?> pType, String getter, String setter,
            Class<?>... argParams) {
        addPropertyMock(new ArtificialProperty(pName, pType, getter, setter, argParams));
    }

    public void addPropertyMock(ArtificialProperty pMock) {
        if (Collections.EMPTY_MAP == properties) {
            properties = new HashMap<String, ArtificialProperty>();
        }
        pMock.setTargetType(type);
        properties.put(pMock.getName(), pMock);
    }

    public void setPropertyUtils(PropertyUtils propertyUtils) {
        this.propertyUtils = propertyUtils;
    }

    /* begin: Representer */
    public void setIncludes(String... propNames) {
        this.includes = (propNames != null && propNames.length > 1) ? propNames : null;
    }

    public void setExcludes(String... propNames) {
        if (propNames != null && propNames.length > 0) {
            excludes = new HashSet<String>();
            for (String name : propNames) {
                excludes.add(name);
            }
        } else {
            excludes = Collections.emptySet();
        }
    }

    public Set<Property> getProperties() {
        if (dumpProperties != null) {
            return dumpProperties;
        }

        if (propertyUtils != null) {
            if (includes != null) {
                dumpProperties = new LinkedHashSet<Property>();
                for (String propertyName : includes) {
                    if (!excludes.contains(propertyName)) {
                        dumpProperties.add(getProperty(propertyName));
                    }
                }
                return dumpProperties;
            }

            final Set<Property> readableProps = (beanAccess == null) ? propertyUtils
                    .getProperties(type) : propertyUtils.getProperties(type, beanAccess);

            if (properties.isEmpty()) {
                if (excludes.isEmpty()) {
                    return dumpProperties = readableProps;
                }
                dumpProperties = new LinkedHashSet<Property>();
                for (Property property : readableProps) {
                    if (!excludes.contains(property.getName())) {
                        dumpProperties.add(property);
                    }
                }
                return dumpProperties;
            }

            if (!delegatesChecked) {
                checkDelegates();
            }

            dumpProperties = new LinkedHashSet<Property>();
            for (Property property : readableProps) {
                if (!excludes.contains(property.getName())) {
                    if (properties.containsKey(property.getName())) {
                        dumpProperties.add(properties.get(property.getName()));
                    } else {
                        dumpProperties.add(property);
                    }
                }
            }
            return dumpProperties;
        }
        return null;
    }

    /* end: Representer */

    /*------------ Maybe something useful to override :) ---------*/

    public boolean setupPropertyType(String key, Node valueNode) {
        return false;
    }

    public boolean setProperty(Object targetBean, String propertyName, Object value)
            throws Exception {
        return false;
    }

    public Object newInstance(Node node) {
        if (impl != null) {
            try {
                java.lang.reflect.Constructor<?> c = impl.getDeclaredConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                impl = null;
            }
        }
        return null;
    }

    public Object newInstance(String propertyName, Node node) {
        return null;
    }
}
