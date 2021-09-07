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
package org.yaml.snakeyaml.issues.issue307;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class OrderTest extends TestCase {

    public void test_order() {
        Representer representer = new Representer();
        representer.setPropertyUtils(new OrderUtil());
        Yaml yaml = new Yaml(representer);
        String s = Util.getLocalResource("issues/issue307-order.yaml");
        OrderBean orderBean = yaml.loadAs(s, OrderBean.class);
        String dump = yaml.dump(orderBean);
        //System.out.println(dump);
        String str = "!!org.yaml.snakeyaml.issues.issue307.OrderBean\n" +
                "name: tian\n" +
                "type: {z: 256, y: 255, x: 254}\n" +
                "age: 22\n" +
                "text: omit\n";
        assertEquals(str,dump);
    }

    public void test_extend_order() {
        Representer representer = new Representer();
        representer.setPropertyUtils(new OrderUtil());
        Yaml yaml = new Yaml(representer);
        String s = Util.getLocalResource("issues/issue307-order.yaml");
        CustomerBean orderBean2 = yaml.loadAs(s, CustomerBean.class);
        orderBean2.setCustomerName("customer");
        String dump = yaml.dump(orderBean2);
        //System.out.println(dump);
        String str = "!!org.yaml.snakeyaml.issues.issue307.CustomerBean\n" +
                "customerName: customer\n" +
                "name: tian\n" +
                "type: {z: 256, y: 255, x: 254}\n" +
                "age: 22\n" +
                "text: omit\n";
        assertEquals(str,dump);
    }

    public static class OrderUtil extends PropertyUtils {
        @Override
        protected Set<Property> createPropertySet(final Class<?> type, BeanAccess bAccess) {
            Set<Property> properties = new TreeSet<>(new Comparator<Property>() {
                @Override
                public int compare(Property prop1, Property prop2) {
                    Integer order1 = getValue(prop1.getName(), type);
                    Integer order2 = getValue(prop2.getName(), type);
                    if (order1 == null) {
                        order1 = 0;
                    }
                    if (order2 == null) {
                        order2 = 0;
                    }
                    if (order1 < order2) {
                        return -1;
                    }
                    if (order1 > order2) {
                        return 1;
                    }
                    return prop1.getName().compareTo(prop2.getName());
                }
            });
            properties.addAll(super.createPropertySet(type, bAccess));
            return properties;
        }

        public Integer getValue(String name, Class<?> type) {
            PropertyUtils propertyUtils = new PropertyUtils();
            propertyUtils.setBeanAccess(BeanAccess.FIELD);
            Property property = propertyUtils.getProperty(type, name);
            Annotation annotation = property.getAnnotation(OrderAnnotation.class);
            Integer invoke = null;
            if (annotation != null) {
                try {
                    Method method = annotation.annotationType().getDeclaredMethod("order");
                    invoke = (Integer) method.invoke(annotation, null);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            return invoke;
        }
    }
}
