package org.yaml.snakeyaml;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides additional runtime information necessary to create a custom Java
 * instance.
 */
public final class TypeDescription {
    private final Class<? extends Object> type;
    private String tag;
    private boolean root;
    private Map<String, Class<? extends Object>> listProperties;
    private Map<String, Class<? extends Object>> keyProperties;
    private Map<String, Class<? extends Object>> valueProperties;

    public TypeDescription(Class<? extends Object> clazz, String tag) {
        this.type = clazz;
        this.tag = tag;
        listProperties = new HashMap<String, Class<? extends Object>>();
        keyProperties = new HashMap<String, Class<? extends Object>>();
        valueProperties = new HashMap<String, Class<? extends Object>>();
    }

    public TypeDescription(Class<? extends Object> clazz) {
        this(clazz, null);
    }

    /**
     * Get tag which shall be used to load or dump the type (class).
     * 
     * @return tag to be used. It may be a tag for Language-Independent Types
     *         (http://www.yaml.org/type/)
     */
    public String getTag() {
        return tag;
    }

    /**
     * Set tag to be used to load or dump the type (class).
     * 
     * @param tag
     *            - local or global tag
     */
    public void setTag(String tag) {
        this.tag = tag;
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
     *            - true if this type shall be used as a root of object
     *            hierarchy.
     */
    public void setRoot(boolean root) {
        this.root = root;
    }

    /**
     * Specify that the property is a type-safe <code>List</code>.
     * 
     * @param property
     *            - name of the JavaBean property
     * @param type
     *            - class of List values
     */
    public void putListPropertyType(String property, Class<? extends Object> type) {
        listProperties.put(property, type);
    }

    /**
     * Get class of List values for provided JavaBean property.
     * 
     * @param property
     *            - property name
     * @return class of List values
     */
    public Class<? extends Object> getListPropertyType(String property) {
        return listProperties.get(property);
    }

    /**
     * Specify that the property is a type-safe <code>Map</code>.
     * 
     * @param property
     *            - property name of this JavaBean
     * @param key
     *            - class of keys in Map
     * @param value
     *            - class of values in Map
     */
    public void putMapPropertyType(String property, Class<? extends Object> key,
            Class<? extends Object> value) {
        keyProperties.put(property, key);
        valueProperties.put(property, value);
    }

    /**
     * Get keys type info for this JavaBean
     * 
     * @param property
     *            - property name of this JavaBean
     * @return class of keys in the Map
     */
    public Class<? extends Object> getMapKeyType(String property) {
        return keyProperties.get(property);
    }

    /**
     * Get values type info for this JavaBean
     * 
     * @param property
     *            - property name of this JavaBean
     * @return class of values in the Map
     */
    public Class<? extends Object> getMapValueType(String property) {
        return valueProperties.get(property);
    }

    @Override
    public String toString() {
        return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
    }
}
