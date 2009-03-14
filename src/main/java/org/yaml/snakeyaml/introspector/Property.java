/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.introspector;

public abstract class Property implements Comparable<Property> {
    private final String name;
    private final Class<? extends Object> type;

    public Property(String name, Class<? extends Object> type) {
        this.name = name;
        this.type = type;
    }

    public Class<? extends Object> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + " of " + getType();
    }

    public int compareTo(Property o) {
        return name.compareTo(o.name);
    }

    abstract public void set(Object object, Object value) throws Exception;

    abstract public Object get(Object object);
}