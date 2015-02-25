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
