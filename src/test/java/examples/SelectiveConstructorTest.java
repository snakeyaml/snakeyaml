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
package examples;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeId;

/**
 * Example for http://code.google.com/p/snakeyaml/wiki/howto
 */
public class SelectiveConstructorTest extends TestCase {
    class SelectiveConstructor extends Constructor {
        public SelectiveConstructor() {
            // define a custom way to create a mapping node
            yamlClassConstructors.put(NodeId.mapping, new MyPersistentObjectConstruct());
        }

        class MyPersistentObjectConstruct extends Constructor.ConstructMapping {
            @Override
            protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
                Class<?> type = node.getType();
                if (type.equals(MyPersistentObject.class)) {
                    // create a map
                    Map<Object, Object> map = constructMapping(node);
                    String id = (String) map.get("id");
                    return new MyPersistentObject(id, 17);
                } else {
                    // create JavaBean
                    return super.constructJavaBean2ndStep(node, object);
                }
            }
        }
    }

    public void testConstructor() {
        Yaml yaml = new Yaml(new SelectiveConstructor());
        List<?> data = (List<?>) yaml
                .load("- 1\n- 2\n- !!examples.MyPersistentObject {amount: 222, id: persistent}");
        // System.out.println(data);
        assertEquals(3, data.size());
        MyPersistentObject myObject = (MyPersistentObject) data.get(2);
        assertEquals(17, myObject.getAmount());
        assertEquals("persistent", myObject.getId());
    }
}

class MyPersistentObject {
    private String id;
    private int amount;

    public MyPersistentObject() {
        this.id = "noid";
        this.amount = 222;
    }

    public MyPersistentObject(String id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "MyPersistentObject [id=" + id + ", amount=" + amount + "]";
    }
}
