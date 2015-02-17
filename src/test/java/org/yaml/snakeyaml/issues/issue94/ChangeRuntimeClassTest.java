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
package org.yaml.snakeyaml.issues.issue94;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;

public class ChangeRuntimeClassTest {

    @Test
    public void testWithGlobalTag() {
        String yamlText = "!!org.yaml.snakeyaml.issues.issue94.Entity\n" + "name: Matt\n"
                + "nickName: Java\n";

        // Now here that I would like to somehow intercept the constructor of
        // SnakeYaml and give it
        // an fresh instance of EntityLoadingProxy(); based on today's
        // temperature, so to speak...
        // that is un-preditable statically which proxy I will give it.

        Yaml yaml = new Yaml(new MyConstructor());

        Entity loadedEntity = null;
        loadedEntity = (Entity) yaml.load(yamlText);

        assertEquals("Matt", loadedEntity.getName());

        // The expectation below is from having intercepted setNickName() with
        // the artifical subclass and
        // performed the calculation.
        assertEquals("JJ-Java", loadedEntity.getNickName());
        assertEquals(EntityLoadingProxy.class, loadedEntity.getClass());
    }

    @Test
    public void testNoTag() {
        String yamlText = "name: Matt\n" + "nickName: Java\n";
        Yaml yaml = new Yaml(new MyConstructor(Entity.class));
        Entity loadedEntity = null;
        loadedEntity = (Entity) yaml.load(yamlText);
        assertEquals("Matt", loadedEntity.getName());
        assertEquals("JJ-Java", loadedEntity.getNickName());
    }

    /**
     * @see Constructor.ConstructYamlObject
     */
    private class MyConstructor extends Constructor {
        public MyConstructor() {
            super();
            this.yamlConstructors.put(null, new ConstructProxy());
        }

        public MyConstructor(Class<?> clazz) {
            super(clazz);
            this.yamlConstructors.put(null, new ConstructProxy());
        }

        private class ConstructProxy extends AbstractConstruct {
            private Construct getConstructor(Node node) {
                Class<?> cl = getClassForNode(node);
                if (cl.equals(Entity.class) && true) {
                    // today's temperature is high :)
                    cl = EntityLoadingProxy.class;
                }
                node.setType(cl);
                // call the constructor as if the runtime class is defined
                Construct constructor = yamlClassConstructors.get(node.getNodeId());
                return constructor;
            }

            public Object construct(Node node) {
                return getConstructor(node).construct(node);
            }
        }
    }
}
