/**
 * Copyright (c) 2008, SnakeYAML
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

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.constructor.TestBean1;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class PropertyTest {

    @Test
    public void testMethodPropertyToString() throws IntrospectionException {
        for (PropertyDescriptor property : Introspector.getBeanInfo(TestBean1.class)
                .getPropertyDescriptors()) {
            if (property.getName().equals("text")) {
                MethodProperty prop = new MethodProperty(property);
                assertEquals("text of class java.lang.String", prop.toString());
            }
        }
    }

    @Test
    public void testGetFieldPropertyAnnotation() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.FIELD);

        Property name = propertyUtils.getProperty(TestBean.class, "name");

        TestAnnotation annotation = name.getAnnotation(TestAnnotation.class);
        assertNotNull(annotation);
        assertEquals("field", annotation.value());
    }

    @Test
    public void testGetFieldPropertyAnnotations() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.FIELD);

        Property property = propertyUtils.getProperty(TestBean.class, "name");

        List<Annotation> annotations = property.getAnnotations();
        assertEquals(1, annotations.size());

        Annotation annotation = annotations.get(0);
        assertEquals(annotation.annotationType(), TestAnnotation.class);
        assertEquals("field", ((TestAnnotation) annotation).value());

    }

    @Test
    public void testGetMethodPropertyAnnotation() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.PROPERTY);

        Property property = propertyUtils.getProperty(TestBean.class, "age");

        TestAnnotation annotation = property.getAnnotation(TestAnnotation.class);
        assertNotNull(annotation);
        assertEquals("getter", annotation.value()); // Annotation on getter takes precedence
    }

    @Test
    public void testGetMethodPropertyAnnotationSetterOnly() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.PROPERTY);

        Property property = propertyUtils.getProperty(TestBean.class, "lastName");
        TestAnnotation annotation = property.getAnnotation(TestAnnotation.class);
        assertNotNull(annotation);
        assertEquals("setter", annotation.value());
    }

    @Test
    public void testGetMethodPropertyAnnotationsSetterOnly() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.PROPERTY);

        Property property = propertyUtils.getProperty(TestBean.class, "lastName");
        List<Annotation> annotations = property.getAnnotations();
        assertEquals(1, annotations.size());

        Annotation annotation = annotations.get(0);

        Assert.assertEquals(TestAnnotation.class, annotation.annotationType());
        Assert.assertEquals("setter", ((TestAnnotation) annotation).value());
    }

    @Test
    public void testGetMethodPropertyAnnotations() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setBeanAccess(BeanAccess.PROPERTY);

        Property age = propertyUtils.getProperty(TestBean.class, "age");

        List<Annotation> annotations = age.getAnnotations();
        assertEquals(2, annotations.size());

        Set<String> expectedValues = new HashSet<String>(Arrays.asList("getter", "setter"));

        for (Annotation annotation : annotations) {
            assertEquals(annotation.annotationType(), TestAnnotation.class);
            String value = ((TestAnnotation) annotation).value();
            Assert.assertTrue("The annotation has unexpected value: " + annotation, expectedValues.remove(value));
        }
    }

    @Test
    public void testGetMissingPropertyAnnotation() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);

        Property property = propertyUtils.getProperty(TestBean.class, "missing");

        assertNull(property.getAnnotation(TestAnnotation.class));
    }

    @Test
    public void testGetMissingPropertyAnnotations() {
        PropertyUtils propertyUtils = new PropertyUtils();
        propertyUtils.setSkipMissingProperties(true);

        Property property = propertyUtils.getProperty(TestBean.class, "missing");

        List<Annotation> annotations = property.getAnnotations();

        assertNotNull(annotations);
        assertTrue(annotations.isEmpty());
    }


}
