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
package org.yaml.snakeyaml.issues.issue437;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

public class BinaryRoundTripTest extends TestCase {

  public void testBinary() throws UnsupportedEncodingException {
    Yaml underTest = new Yaml();
    String source = "\u0096";
    String serialized = underTest.dump(source);
    assertEquals("!!binary |-\n" + "  wpY=\n", serialized);
    // parse back to bytes
    byte[] deserialized = underTest.load(serialized);
    assertEquals(source, new String(deserialized, StandardCharsets.UTF_8));
  }

  public void testBinaryNode() {
    Yaml underTest = new Yaml();
    String source = "\u0096";
    Node node = underTest.represent(source);
    // check Node
    assertEquals(Tag.BINARY, node.getTag());
    assertEquals(NodeId.scalar, node.getNodeId());
    ScalarNode scalar = (ScalarNode) node;
    assertEquals("wpY=", scalar.getValue());
    // check Event
    List<Event> events = underTest.serialize(node);
    assertEquals(5, events.size());
    ScalarEvent data = (ScalarEvent) events.get(2);
    assertEquals(Tag.BINARY.toString(), data.getTag());
    assertEquals(DumperOptions.ScalarStyle.LITERAL, data.getScalarStyle());
    assertEquals("wpY=", data.getValue());
    ImplicitTuple implicit = data.getImplicit();
    assertFalse(implicit.canOmitTagInPlainScalar());
    assertFalse(implicit.canOmitTagInNonPlainScalar());
  }

  public void testStrNode() {
    DumperOptions options = new DumperOptions();
    options.setNonPrintableStyle(DumperOptions.NonPrintableStyle.ESCAPE);
    Yaml underTest = new Yaml(options);
    String source = "\u0096";
    Node node = underTest.represent(source);
    assertEquals(Tag.STR, node.getTag());
    assertEquals(NodeId.scalar, node.getNodeId());
    ScalarNode scalar = (ScalarNode) node;
    assertEquals("\u0096", scalar.getValue());
  }

  public void testRoundTripBinary() {
    DumperOptions options = new DumperOptions();
    options.setNonPrintableStyle(DumperOptions.NonPrintableStyle.ESCAPE);
    Yaml underTest = new Yaml(options);
    Map<String, String> toSerialized = new HashMap<>();
    toSerialized.put("key", "a\u0096b");
    String output = underTest.dump(toSerialized);
    assertEquals("{key: \"a\\x96b\"}\n", output);
    Map<String, String> parsed = underTest.load(output);
    assertEquals(toSerialized.get("key"), parsed.get("key"));
    assertEquals(toSerialized, parsed);
  }
}
