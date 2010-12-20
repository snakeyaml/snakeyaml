/**
 * Copyright (c) 2008-2010, http://www.snakeyaml.org
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yaml.snakeyaml.error.YAMLException;

// TODO: decide priorities for get/set Read/Field/Delegate Write/Field/Delegate - is FIELD on the correct place ?
public class ArtificialProperty extends Property {

    final private static Logger log = Logger.getLogger(ArtificialProperty.class.getPackage()
            .getName());

    protected Class<?> targetType;
    private final String readMethod;
    private final String writeMethod;
    transient private Method read;
    transient private Method write;
    private Field field;

    public ArtificialProperty(String name, Class<?> type, String readMethod, String writeMethod,
            Class<?>... params) {
        super(name, type);
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.parameters = params;
    }

    public ArtificialProperty(String name, Class<?> type, Class<?>... params) {
        this(name, type, null, null, params);
    }

    protected Class<?>[] parameters;
    private Property delegate;

    @Override
    public Class<?>[] getActualTypeArguments() {
        if (parameters == null && delegate != null) {
            return delegate.getActualTypeArguments();
        }
        return parameters;
    }

    public void setActualTypeArguments(Class<?>... args) {
        parameters = args;
    }

    @Override
    public void set(Object object, Object value) throws Exception {
        if (write != null) {
            write.invoke(object, value);
        } else if (field != null) {
            field.set(object, value);
        } else if (delegate != null) {
            delegate.set(object, value);
        } else {
            log.warning("No setter/delegate for '" + getName() + "' on object " + object);
        }
    }

    @Override
    public Object get(Object object) {
        try {
            if (read != null) {
                return read.invoke(object);
            } else if (field != null) {
                return field.get(object);
            }
        } catch (Exception e) {
            throw new YAMLException("Unable to find getter for property '" + getName()
                    + "' on object " + object + ":" + e);
        }

        if (delegate != null) {
            return delegate.get(object);
        }
        throw new YAMLException("No getter or delegate for property '" + getName() + "' on object "
                + object);
    }

    public void setTargetType(Class<?> targetType) {
        if (this.targetType != targetType) {
            this.targetType = targetType;

            try {
                field = targetType.getDeclaredField(getName());
                field.setAccessible(true);
            } catch (Exception e) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine(targetType.getName() + "." + getName() + " : "
                            + e.getClass().getName());
                    if (log.isLoggable(Level.FINEST)) {
                        e.printStackTrace();
                    }
                }
            }

            // Retrieve needed info
            if (readMethod != null) {
                try {
                    read = targetType.getDeclaredMethod(readMethod);
                    read.setAccessible(true);
                } catch (Exception e) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(targetType.getName() + "." + getName() + " : "
                                + e.getClass().getName());
                        if (log.isLoggable(Level.FINEST)) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (writeMethod != null) {
                try {
                    write = targetType.getDeclaredMethod(writeMethod, getType());
                    write.setAccessible(true);
                } catch (Exception e) {
                    if (log.isLoggable(Level.FINE)) {
                        log.fine(targetType.getName() + "." + getName() + " : "
                                + e.getClass().getName());
                        if (log.isLoggable(Level.FINEST)) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getName() {
        final String n = super.getName();
        if (n != null) {
            return n;
        }
        return delegate != null ? delegate.getName() : null;
    }

    @Override
    public Class<?> getType() {
        final Class<?> t = super.getType();
        if (t != null) {
            return t;
        }
        return delegate != null ? delegate.getType() : null;
    }

    @Override
    public boolean isReadable() {
        return (read != null) || (field != null) || (delegate != null && delegate.isReadable());
    }

    @Override
    public boolean isWritable() {
        return (write != null) || (field != null) || (delegate != null && delegate.isWritable());
    }

    public void setDelegate(Property delegate) {
        this.delegate = delegate;
    }

}
