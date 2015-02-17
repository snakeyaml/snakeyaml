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
package org.yaml.snakeyaml.issues.issue29;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class FlexibleScalarStylesInJavaBeanTest extends TestCase {
    public void testDifferentStyles() {
        BigJavaBean bean1 = new BigJavaBean(1, "simple", "line 1\nline2\nzipcode", "short text1");
        List<Integer> numbers1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        bean1.setNumbers(numbers1);
        Map<String, String> data1 = new HashMap<String, String>();
        data1.put("key1", "value1");
        data1.put("key2", "value2");
        bean1.setData(data1);
        //
        BigJavaBean bean2 = new BigJavaBean(1, "second", "line 111\nline 222\nzipcode 12345\n\n",
                "info: semicolon is used");
        List<Integer> numbers2 = new ArrayList<Integer>(Arrays.asList(4, 5, 6, 777, 888, 999, 1000));
        bean2.setNumbers(numbers2);
        Map<String, String> data2 = new HashMap<String, String>();
        data2.put("key21", "value12");
        data2.put("key22", "value with\ntwo lines");
        bean2.setData(data2);
        //
        List<BigJavaBean> list = new ArrayList<BigJavaBean>();
        list.add(bean1);
        list.add(bean2);
        Yaml yaml = new Yaml(new MyRepresenter());
        yaml.setBeanAccess(BeanAccess.FIELD);
        String output = yaml.dump(list);
        // System.out.println(output);
        // parse back
        @SuppressWarnings("unchecked")
        List<BigJavaBean> parsed = (List<BigJavaBean>) yaml.load(output);
        assertEquals(2, parsed.size());
        assertEquals(bean1, parsed.get(0));
        assertEquals(bean2, parsed.get(1));

    }

    private class MyRepresenter extends Representer {
        /*
         * Change the default order. Important data goes first.
         */
        @Override
        protected Set<Property> getProperties(Class<? extends Object> type)
                throws IntrospectionException {
            if (type.isAssignableFrom(BigJavaBean.class)) {
                Set<Property> standard = super.getProperties(type);
                Set<Property> sorted = new TreeSet<Property>(new PropertyComparator());
                sorted.addAll(standard);
                return sorted;
            } else {
                return super.getProperties(type);
            }
        }

        private class PropertyComparator implements Comparator<Property> {
            public int compare(Property o1, Property o2) {
                // important go first
                List<String> order = new ArrayList<String>(Arrays.asList("id", "name",
                        "description", "address"));
                for (String name : order) {
                    int c = compareByName(o1, o2, name);
                    if (c != 0) {
                        return c;
                    }
                }
                // all the rest
                return o1.compareTo(o2);
            }

            private int compareByName(Property o1, Property o2, String name) {
                if (o1.getName().equals(name)) {
                    return -1;
                } else if (o2.getName().equals(name)) {
                    return 1;
                }
                return 0;// compare further
            }
        }

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                Object propertyValue, Tag customTag) {
            if (javaBean instanceof BigJavaBean) {
                BigJavaBean bean = (BigJavaBean) javaBean;
                NodeTuple standard = super.representJavaBeanProperty(javaBean, property,
                        propertyValue, customTag);
                if (property.getName().equals("numbers")) {
                    // when the list is small, make it block collection style
                    if (bean.getNumbers().size() < 5) {
                        SequenceNode n = (SequenceNode) standard.getValueNode();
                        return new NodeTuple(standard.getKeyNode(), new SequenceNode(n.getTag(),
                                true, n.getValue(), n.getStartMark(), n.getEndMark(), false));
                    }
                }
                if (property.getName().equals("description")) {
                    // if description contains ':' use folded scalar style and
                    // not single quoted scalar style
                    if (bean.getDescription().indexOf(':') > 0) {
                        ScalarNode n = (ScalarNode) standard.getValueNode();
                        return new NodeTuple(standard.getKeyNode(), new ScalarNode(n.getTag(),
                                n.getValue(), n.getStartMark(), n.getEndMark(), '>'));
                    }
                }
                return standard;
            } else {
                return super
                        .representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }
    }
}
