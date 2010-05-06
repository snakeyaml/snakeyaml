package org.yaml.snakeyaml.introspector;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract public class GenericProperty extends Property {

    private Type genType;

    public GenericProperty(String name, Class<?> aClass, Type aType) {
        super(name, aClass);
        genType = aType;
        actualClassesChecked = aType == null;
    }

    private boolean actualClassesChecked;
    private Class<?>[] actualClasses;

    public Class<?>[] getActualTypeArguments() { // should we synchronize here ?
        if (!actualClassesChecked) {
            if (genType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments.length > 0) {
                    actualClasses = new Class<?>[actualTypeArguments.length];
                    for (int i = 0; i < actualTypeArguments.length; i++) {
                        if (actualTypeArguments[i] instanceof Class<?>) {
                            actualClasses[i] = (Class<?>) actualTypeArguments[i];
                        } else {
                            actualClasses = null;
                            break;
                        }
                    }
                }
            } else if (genType instanceof GenericArrayType) {
                Type componentType = ((GenericArrayType) genType).getGenericComponentType();
                if (componentType instanceof Class<?>) {
                    actualClasses = new Class<?>[] { (Class<?>) componentType };
                }
            }
            actualClassesChecked = true;
        }
        return actualClasses;
    }

}
