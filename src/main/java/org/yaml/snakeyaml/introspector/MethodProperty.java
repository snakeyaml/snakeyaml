/**
 * Copyright (c) 2008, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.introspector;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.yaml.snakeyaml.error.YAMLException;

/**
 * <p>
 * A <code>MethodProperty</code> is a <code>Property</code> which is accessed
 * through accessor methods (setX, getX). It is possible to have a
 * <code>MethodProperty</code> which has only setter, only getter, or both. It
 * is not possible to have a <code>MethodProperty</code> which has neither
 * setter nor getter.
 * </p>
 */
public class MethodProperty extends GenericProperty {

    private final PropertyDescriptor property;
    private final boolean readable;
    private final boolean writable;

    private static Type discoverGenericType(PropertyDescriptor property) {
        Method readMethod = property.getReadMethod();
        if (readMethod != null) {
            return readMethod.getGenericReturnType();
        }

        Method writeMethod = property.getWriteMethod();
        if (writeMethod != null) {
            Type[] paramTypes = writeMethod.getGenericParameterTypes();
            if (paramTypes.length > 0) {
                return paramTypes[0];
            }
        }
        /*
         * This actually may happen if PropertyDescriptor is of type
         * IndexedPropertyDescriptor and it has only IndexedGetter/Setter. ATM
         * we simply skip type discovery.
         */
        return null;
    }

    public MethodProperty(PropertyDescriptor property) {
        super(property.getName(), property.getPropertyType(),
                MethodProperty.discoverGenericType(property));
        this.property = property;
        this.readable = property.getReadMethod() != null;
        this.writable = property.getWriteMethod() != null;
    }

    @Override
    public void set(Object object, Object value) throws Exception {
        if (!writable) {
            throw new YAMLException("No writable property '" + getName() + "' on class: "
                    + object.getClass().getName());
        }
        property.getWriteMethod().invoke(object, value);
    }

    @Override
    public Object get(Object object) {
        try {
            property.getReadMethod().setAccessible(true);// issue 50
            return property.getReadMethod().invoke(object);
        } catch (Exception e) {
            throw new YAMLException("Unable to find getter for property '" + property.getName()
                    + "' on object " + object + ":" + e);
        }
    }

    @Override
    public boolean isWritable() {
        return writable;
    }

    @Override
    public boolean isReadable() {
        return readable;
    }
}