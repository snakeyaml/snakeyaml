/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.introspector;

import java.beans.PropertyDescriptor;

import org.yaml.snakeyaml.error.YAMLException;

public class MethodProperty extends Property {
    private final PropertyDescriptor property;

    public MethodProperty(PropertyDescriptor property) {
        super(property.getName(), property.getPropertyType());
        this.property = property;
    }

    @Override
    public void set(Object object, Object value) throws Exception {
        property.getWriteMethod().invoke(object, value);
    }

    @Override
    public Object get(Object object) {
        try {
            return property.getReadMethod().invoke(object);
        } catch (Exception e) {
            throw new YAMLException("Unable to find getter for property " + property.getName()
                    + " on object " + object + ":" + e);
        }
    }
}