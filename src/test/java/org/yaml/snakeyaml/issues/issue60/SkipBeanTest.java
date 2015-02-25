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

import java.util.Arrays;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.CollectionNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class SkipBeanTest extends TestCase {

    public void testSkipNull() {
        Yaml yaml = new Yaml(new SkipNullRepresenter());
        String output = yaml.dump(getBean());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue60-1.yaml"), output);
    }

    private class SkipNullRepresenter extends Representer {
        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                Object propertyValue, Tag customTag) {
            if (propertyValue == null) {
                return null;
            } else {
                return super
                        .representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }
    }

    public void testSkipEmptyCollections() {
        Yaml yaml = new Yaml(new SkipEmptyRepresenter());
        String output = yaml.dump(getBean());
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue60-2.yaml"), output);
    }

    private class SkipEmptyRepresenter extends Representer {
        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                Object propertyValue, Tag customTag) {
            NodeTuple tuple = super.representJavaBeanProperty(javaBean, property, propertyValue,
                    customTag);
            Node valueNode = tuple.getValueNode();
            if (Tag.NULL.equals(valueNode.getTag())) {
                return null;// skip 'null' values
            }
            if (valueNode instanceof CollectionNode) {
                if (Tag.SEQ.equals(valueNode.getTag())) {
                    SequenceNode seq = (SequenceNode) valueNode;
                    if (seq.getValue().isEmpty()) {
                        return null;// skip empty lists
                    }
                }
                if (Tag.MAP.equals(valueNode.getTag())) {
                    MappingNode seq = (MappingNode) valueNode;
                    if (seq.getValue().isEmpty()) {
                        return null;// skip empty maps
                    }
                }
            }
            return tuple;
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
