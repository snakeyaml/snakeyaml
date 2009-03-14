package org.yaml.snakeyaml.introspector;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.TestBean1;

public class MethodPropertyTest extends TestCase {

    public void testToString() throws IntrospectionException {
        for (PropertyDescriptor property : Introspector.getBeanInfo(TestBean1.class)
                .getPropertyDescriptors()) {
            if (property.getName().equals("text")) {
                MethodProperty prop = new MethodProperty(property);
                assertEquals("text of class java.lang.String", prop.toString());
            }
        }
    }
}
