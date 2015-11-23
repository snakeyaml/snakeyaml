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
package org.yaml.snakeyaml.issues.issue311;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import static org.junit.Assert.assertEquals;

public class BooleanEnumTest {

    @Test
    public void loadEnum() {

        Yaml yaml = new Yaml(new MyConstructor(), new MyRepresenter());
        BeanWithEnum parsed = yaml.loadAs("{boolField: true, enumField: true, name: '10'}", BeanWithEnum.class);
        //System.out.println(parsed.getEnumField());
        assertEquals(BooleanEnum.TRUE, parsed.getEnumField());
        assertEquals("10", parsed.getName());
    }

    @Test
    public void loadEnumUndefined() {

        Yaml yaml = new Yaml(new MyConstructor(), new MyRepresenter());
        BeanWithEnum parsed = yaml.loadAs("{boolField: true, enumField: nonsense, name: bar}", BeanWithEnum.class);
        //System.out.println(parsed.getEnumField());
        assertEquals(BooleanEnum.UNKNOWN, parsed.getEnumField());
        assertEquals("bar", parsed.getName());
    }

    @Test
    public void dumpEnum() {

        BeanWithEnum bean = new BeanWithEnum(true, "10", BooleanEnum.TRUE);
        Yaml yaml = new Yaml(new MyConstructor(), new MyRepresenter());
        String output = yaml.dumpAs(bean, Tag.MAP, DumperOptions.FlowStyle.FLOW);
        assertEquals("{boolField: true, enumField: 'true', name: '10'}\n", output);
    }

    class MyRepresenter extends Representer {
        public MyRepresenter() {
            this.representers.put(BooleanEnum.class, new RepresentEnum());
        }

        private class RepresentEnum implements Represent {
            public Node representData(Object data) {
                BooleanEnum myEnum = (BooleanEnum) data;
                String value;
                switch (myEnum) {
                    case TRUE:
                        value = "true";
                        break;

                    case FALSE:
                        value = "false";
                        break;

                    case UNKNOWN:
                        value = "unknown";
                        break;

                    default:
                        throw new IllegalArgumentException();
                }
                return representScalar(Tag.STR, value);
            }
        }
    }

    class MyConstructor extends Constructor {
        public MyConstructor() {
            this.yamlClassConstructors.put(NodeId.scalar, new ConstructEnum());
        }

        private class ConstructEnum extends ConstructScalar {
            public Object construct(Node node) {
                if (node.getType().equals(BooleanEnum.class)) {
                    String val = (String) constructScalar((ScalarNode) node);
                    if ("true".equals(val)) {
                        return BooleanEnum.TRUE;
                    } else if ("false".equals(val)) {
                        return BooleanEnum.FALSE;
                    } else
                        return BooleanEnum.UNKNOWN;
                }
                return super.construct(node);
            }
        }
    }
}
