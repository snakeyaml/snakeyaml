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
package org.yaml.snakeyaml.issues.issue544;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class DoubleQuoteTest {

  private MappingNode create() {
    String content = "🔐This process is simple and secure.";

    ScalarNode doubleQuotedKey =
        new ScalarNode(Tag.STR, "double_quoted", null, null, ScalarStyle.PLAIN);
    ScalarNode doubleQuotedValue =
        new ScalarNode(Tag.STR, content, null, null, ScalarStyle.DOUBLE_QUOTED);
    NodeTuple doubleQuotedTuple = new NodeTuple(doubleQuotedKey, doubleQuotedValue);

    ScalarNode singleQuotedKey =
        new ScalarNode(Tag.STR, "single_quoted", null, null, ScalarStyle.PLAIN);
    ScalarNode singleQuotedValue =
        new ScalarNode(Tag.STR, content, null, null, ScalarStyle.SINGLE_QUOTED);
    NodeTuple singleQuotedTuple = new NodeTuple(singleQuotedKey, singleQuotedValue);

    List<NodeTuple> nodeTuples = new ArrayList<>();
    nodeTuples.add(doubleQuotedTuple);
    nodeTuples.add(singleQuotedTuple);

    MappingNode mappingNode = new MappingNode(Tag.MAP, nodeTuples, FlowStyle.BLOCK);

    return mappingNode;
  }

  private String emit(DumperOptions dumperOptions) {
    Yaml yaml = new Yaml(dumperOptions);

    StringWriter writer = new StringWriter();
    yaml.serialize(create(), writer);

    return writer.toString();
  }

  @Test
  public void testUnicode() {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setAllowUnicode(true); // use as is
    String output = emit(dumperOptions);
    String expectedOutput = "double_quoted: \"🔐This process is simple and secure.\"\n"
        + "single_quoted: '🔐This process is simple and secure.'\n";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSubstitution() {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setAllowUnicode(false); // substitute with U notation
    String output = emit(dumperOptions);
    String expectedOutput = "double_quoted: \"\\U0001f510This process is simple and secure.\"\n"
        + "single_quoted: \"\\U0001f510This process is simple and secure.\"\n";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testDefault() {
    DumperOptions dumperOptions = new DumperOptions();
    String output = emit(dumperOptions);
    String expectedOutput = "double_quoted: \"🔐This process is simple and secure.\"\n"
        + "single_quoted: '🔐This process is simple and secure.'\n";

    assertEquals(expectedOutput, output);
  }
}
