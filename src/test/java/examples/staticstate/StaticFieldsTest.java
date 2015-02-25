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
package examples.staticstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Example with static fields
 */
public class StaticFieldsTest extends TestCase {
    public void testAsJavaBean() {
        JavaBeanWithStaticState bean = new JavaBeanWithStaticState();
        bean.setName("Bahrack");
        bean.setAge(-47);
        JavaBeanWithStaticState.setType("Represent");
        JavaBeanWithStaticState.color = "Black";
        Yaml yaml = new Yaml();
        String output = yaml.dump(bean);
        // System.out.println(output);
        assertEquals("!!examples.staticstate.JavaBeanWithStaticState {age: -47, name: Bahrack}\n",
                output);
        // parse back to instance
        JavaBeanWithStaticState bean2 = (JavaBeanWithStaticState) yaml.load(output);
        assertEquals(-47, bean2.getAge());
        assertEquals("Bahrack", bean2.getName());
    }

    public void testCustomDump() {
        JavaBeanWithStaticState bean = new JavaBeanWithStaticState();
        bean.setName("Lui");
        bean.setAge(25);
        JavaBeanWithStaticState.setType("Represent");
        JavaBeanWithStaticState.color = "Black";
        Yaml yaml = new Yaml(new MyRepresenter(), new DumperOptions());
        String output = yaml.dump(bean);
        // System.out.println(output);
        assertEquals(
                "!!examples.staticstate.JavaBeanWithStaticState {age: 25, name: Lui, color: Black,\n  type: Represent}\n",
                output);
    }

    public void testCustomLoad() {
        Yaml yaml = new Yaml(new MyConstructor());
        String output = "!!examples.staticstate.JavaBeanWithStaticState {age: 25, name: Lui, color: Oranje,\n  type: King}\n";
        JavaBeanWithStaticState bean2 = (JavaBeanWithStaticState) yaml.load(output);
        assertEquals(25, bean2.getAge());
        assertEquals("Lui", bean2.getName());
        assertEquals("Oranje", JavaBeanWithStaticState.color);
        assertEquals("King", JavaBeanWithStaticState.getType());
    }

    private class MyRepresenter extends Representer {
        @Override
        protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
            MappingNode node = super.representJavaBean(properties, javaBean);
            if (javaBean instanceof JavaBeanWithStaticState) {
                List<NodeTuple> value = node.getValue();
                value.add(new NodeTuple(representData("color"),
                        representData(JavaBeanWithStaticState.color)));
                value.add(new NodeTuple(representData("type"),
                        representData(JavaBeanWithStaticState.getType())));
            }
            return node;
        }
    }

    private class MyConstructor extends Constructor {
        protected Object constructObject(Node node) {
            if (node.getType().isAssignableFrom(JavaBeanWithStaticState.class)) {
                MappingNode beanNode = (MappingNode) node;
                List<NodeTuple> value = beanNode.getValue();
                List<NodeTuple> removed = new ArrayList<NodeTuple>();
                for (NodeTuple tuple : value) {
                    ScalarNode keyNode = (ScalarNode) tuple.getKeyNode();
                    if (keyNode.getValue().equals("color")) {
                        ScalarNode valueNode = (ScalarNode) tuple.getValueNode();
                        JavaBeanWithStaticState.color = valueNode.getValue();
                    } else if (keyNode.getValue().equals("type")) {
                        ScalarNode valueNode = (ScalarNode) tuple.getValueNode();
                        JavaBeanWithStaticState.setType(valueNode.getValue());
                    } else
                        removed.add(tuple);
                }
                beanNode.setValue(removed);
                JavaBeanWithStaticState bean = (JavaBeanWithStaticState) super
                        .constructObject(beanNode);

                return bean;
            } else {
                return super.constructObject(node);
            }
        }
    }
}
