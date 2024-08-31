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
package org.yaml.snakeyaml.issues.issue1086;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlCreator;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.recursive.Human3;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.serializer.SerializerException;

public class DereferenceAliasesTest {

  @Test
  public void noAliases_Compose_Serialize() {
    String str = Util.getLocalResource("issues/issue1086-input.yaml");
    String expected = Util.getLocalResource("issues/issue1086-expected.yaml");
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setDereferenceAliases(true);

    Yaml yaml = new Yaml(options);

    Node node = yaml.compose(new StringReader(str));
    StringWriter out = new StringWriter();
    yaml.serialize(node, out);
    assertEquals(expected, out.toString());
  }

  @Test
  public void noAliases_Load_Serialize() {
    String str = Util.getLocalResource("issues/issue1086-input.yaml");
    String expected = Util.getLocalResource("issues/issue1086-expected.yaml");
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setDereferenceAliases(true);

    Yaml yaml = new Yaml(options);

    Object loaded = yaml.load(new StringReader(str));
    String dump = yaml.dump(loaded);
    assertEquals(expected, dump);
  }

  @Test
  public void noAliases_Recursive_Compose_Serialize() {
    final String str = Util.getLocalResource("recursive/with-children-3.yaml");
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setDereferenceAliases(true);
    Constructor constructor =
        new Constructor(Human3.class, YamlCreator.trustPrefixLoaderOptions("org.yaml.snakeyaml"));

    final Yaml yaml = new Yaml(constructor, new Representer(options), options);
    final Node node = yaml.compose(new StringReader(str));

    SerializerException exception = assertThrows(SerializerException.class, new ThrowingRunnable() {
      @Override
      public void run() throws Throwable {
        StringWriter out = new StringWriter();
        yaml.serialize(node, out);
      }
    });
    assertEquals("Cannot dereferenceAliases for recursive structures.",
        exception.getLocalizedMessage());
  }

  @Test
  public void noAliases_Recursive_Load_Dump() {
    String str = Util.getLocalResource("recursive/with-children-3.yaml");
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    options.setDereferenceAliases(true);

    Constructor constructor =
        new Constructor(Human3.class, YamlCreator.trustPrefixLoaderOptions("org.yaml.snakeyaml"));
    TypeDescription human3Description = new TypeDescription(Human3.class);
    human3Description.addPropertyParameters("children", Human3.class);
    constructor.addTypeDescription(human3Description);

    final Yaml yaml = new Yaml(constructor, new Representer(options), options);
    final Human3 human = yaml.load(str);

    SerializerException exception = assertThrows(SerializerException.class, new ThrowingRunnable() {
      @Override
      public void run() throws Throwable {
        yaml.dump(human);
      }
    });
    assertEquals("Cannot dereferenceAliases for recursive structures.",
        exception.getLocalizedMessage());
  }

}
