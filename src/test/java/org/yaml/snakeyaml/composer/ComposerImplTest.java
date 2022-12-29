/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.composer;

import static org.junit.Assert.assertNotEquals;

import java.io.StringReader;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlCreator;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;

public class ComposerImplTest extends TestCase {

  public void testGetNode() {
    String data = "american:\n  - Boston Red Sox";
    Yaml yaml = new Yaml();
    Node node = yaml.compose(new StringReader(data));
    assertNotNull(node);
    assertTrue(node instanceof MappingNode);
    String data2 = "---\namerican:\n- Boston Red Sox";
    Node node2 = yaml.compose(new StringReader(data2));
    assertNotNull(node2);
    assertNotEquals(node, node2);
  }

  public void testComposeBean() {
    String data =
        "!!org.yaml.snakeyaml.composer.ComposerImplTest$BeanToCompose {name: Bill, age: 18}";
    Yaml yaml = YamlCreator.allowClassPrefix("org.yaml.snakeyaml");
    Node node = yaml.compose(new StringReader(data));
    assertNotNull(node);
    assertTrue(node instanceof MappingNode);
    assertEquals("tag:yaml.org,2002:org.yaml.snakeyaml.composer.ComposerImplTest$BeanToCompose",
        node.getTag().getValue());
    assertEquals(NodeId.mapping, node.getNodeId());
    assertEquals(Object.class, node.getType());
  }

  public void testNodeAnchor() {
    String data = "--- &113\n{name: Bill, age: 18}";
    Yaml yaml = new Yaml();
    Node node = yaml.compose(new StringReader(data));
    assertNotNull(node);
    assertTrue(node instanceof MappingNode);
    assertEquals("113", node.getAnchor());
  }

  public static class BeanToCompose {

    private String name;
    private int age;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
}
