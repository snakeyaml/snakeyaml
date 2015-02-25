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
package org.yaml.snakeyaml.representer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class RepresentTest extends TestCase {

    public void testCustomRepresenter() {
        Yaml yaml = new Yaml(new MyConstructor(), new MyRepresenter());
        CustomBean etalon = new CustomBean("A", 1);
        String output = yaml.dump(etalon);
        assertEquals("!!Dice 'Ad1'\n", output);
        CustomBean bean = (CustomBean) yaml.load(output);
        assertEquals("A", bean.getPrefix());
        assertEquals(1, bean.getSuffix());
        assertEquals(etalon, bean);
    }

    class CustomBean {
        private String prefix;
        private int suffix;

        public CustomBean(String prefix, int suffix) {
            this.prefix = prefix;
            this.suffix = suffix;
        }

        public String getPrefix() {
            return prefix;
        }

        public int getSuffix() {
            return suffix;
        }

        @Override
        public boolean equals(Object obj) {
            CustomBean bean = (CustomBean) obj;
            return prefix.equals(bean.getPrefix()) && suffix == bean.getSuffix();
        }
    }

    class MyRepresenter extends Representer {
        public MyRepresenter() {
            this.representers.put(CustomBean.class, new RepresentDice());
        }

        private class RepresentDice implements Represent {
            public Node representData(Object data) {
                CustomBean coin = (CustomBean) data;
                String value = coin.getPrefix() + "d" + coin.getSuffix();
                return representScalar(new Tag("!!Dice"), value);
            }
        }
    }

    class MyConstructor extends Constructor {
        public MyConstructor() {
            this.yamlConstructors.put(new Tag(Tag.PREFIX + "Dice"), new ConstructDice());
        }

        private class ConstructDice extends AbstractConstruct {
            public Object construct(Node node) {
                String val = (String) constructScalar((ScalarNode) node);
                return new CustomBean(val.substring(0, 1), new Integer(val.substring(2)));
            }
        }
    }
}
