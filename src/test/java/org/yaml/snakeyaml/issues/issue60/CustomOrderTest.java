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
package org.yaml.snakeyaml.issues.issue60;

import java.beans.IntrospectionException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

//issue 59
public class CustomOrderTest extends TestCase {

    public void testReversedOrder() {
        Representer repr = new Representer();
        repr.setPropertyUtils(new ReversedPropertyUtils());
        Yaml yaml = new Yaml(repr);
        String output = yaml.dump(getBean());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue59-1.yaml"), output);
    }

    private class ReversedPropertyUtils extends PropertyUtils {
        @Override
        protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess)
                throws IntrospectionException {
            Set<Property> result = new TreeSet<Property>(Collections.reverseOrder());
            result.addAll(super.createPropertySet(type, bAccess));
            return result;
        }
    }

    public void testUnsorted() {
        Representer repr = new Representer();
        repr.setPropertyUtils(new UnsortedPropertyUtils());
        Yaml yaml = new Yaml(repr);
        String output = yaml.dump(getBean());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue59-2.yaml"), output);
    }

    private class UnsortedPropertyUtils extends PropertyUtils {
        @Override
        protected Set<Property> createPropertySet(Class<? extends Object> type, BeanAccess bAccess)
                throws IntrospectionException {
            Set<Property> result = new LinkedHashSet<Property>(getPropertiesMap(type,
                    BeanAccess.FIELD).values());
            result.remove(result.iterator().next());// drop 'listInt' property
            return result;
        }
    }

    private SkipBean getBean() {
        SkipBean bean = new SkipBean();
        bean.setText("foo");
        bean.setListDate(null);
        bean.setListInt(Arrays.asList(new Integer[] { null, 1, 2, 3 }));
        bean.setListStr(Arrays.asList(new String[] { "bar", null, "foo", null }));
        return bean;
    }
}
