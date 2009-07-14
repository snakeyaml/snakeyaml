/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.generics;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class JvmDetector {
    /**
     * Check whether the proper class Nest for Bird's property 'home' is
     * recognized.
     */
    public static boolean isProperIntrospection() throws IntrospectionException {
        for (PropertyDescriptor property : Introspector.getBeanInfo(Bird.class)
                .getPropertyDescriptors()) {
            if (property.getName().equals("home")) {
                return property.getPropertyType() == Nest.class;
            }
        }
        throw new RuntimeException("Bird must contain 'home' property.");
    }
}
