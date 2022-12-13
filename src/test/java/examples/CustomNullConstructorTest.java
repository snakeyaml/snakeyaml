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
package examples;

import junit.framework.TestCase;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Issue 1 for snakeyaml-engine
 * https://bitbucket.org/snakeyaml/snakeyaml-engine/issues/1/null-tag-constructor-not-called-when
 */
public class CustomNullConstructorTest extends TestCase {

  public void testEmpty() {
    Yaml yaml = new Yaml(new NullConstructor());
    assertEquals(Integer.valueOf(1), yaml.load(""));
  }

  public void testNull() {
    Yaml yaml = new Yaml(new NullConstructor());
    assertEquals(Integer.valueOf(1), yaml.load("null"));
  }

  public void testNullTag() {
    Yaml yaml = new Yaml(new NullConstructor());
    assertEquals(Integer.valueOf(1), yaml.load("!!null null"));
  }

  class NullConstructor extends SafeConstructor {

    public NullConstructor() {
      super(new LoaderOptions());
      this.yamlConstructors.put(Tag.NULL, new ConstructNull());
    }

    private class ConstructNull extends AbstractConstruct {

      public Object construct(Node node) {
        return 1;
      }
    }
  }
}
