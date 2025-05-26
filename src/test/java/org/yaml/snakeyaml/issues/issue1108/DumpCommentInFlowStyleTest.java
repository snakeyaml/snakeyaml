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
package org.yaml.snakeyaml.issues.issue1108;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.representer.Representer;

public class DumpCommentInFlowStyleTest {

  private static DumperOptions getDumperOptions() {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setProcessComments(true);
    dumperOptions.setIndent(2);
    dumperOptions.setIndicatorIndent(2);
    dumperOptions.setIndentWithIndicator(true);
    // Flow Style will be inside the Node
    dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
    return dumperOptions;
  }

  private static LoaderOptions getLoaderOptions() {
    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setProcessComments(true);
    loaderOptions.setAllowDuplicateKeys(true);
    loaderOptions.setAllowRecursiveKeys(true);
    loaderOptions.setNestingDepthLimit(1000);
    return loaderOptions;
  }

  private static Yaml getYaml() {
    LoaderOptions loaderOptions = getLoaderOptions();
    DumperOptions dumperOptions = getDumperOptions();
    return new Yaml(new Constructor(loaderOptions), new Representer(dumperOptions), dumperOptions,
        loaderOptions);
  }

  private String extractInlineComment(Node node) {
    MappingNode mapping = (MappingNode) node;
    List<NodeTuple> v = mapping.getValue();
    NodeTuple first = v.get(0);
    Node textNode = first.getValueNode();
    return textNode.getInLineComments().get(0).getValue();
  }

  @Test
  public void readAndWriteCommentsInFlowStyle() {
    Yaml yaml = getYaml();
    String content = "{ url: text # comment breaks it\n}";
    Node node = yaml.compose(new StringReader(content));
    assertEquals(" comment breaks it", extractInlineComment(node));
    StringWriter output = new StringWriter();
    try {
      yaml.serialize(node, output);
      fail(); // TODO issue 1108
    } catch (Exception e) {
      assertTrue(e.getMessage(), e.getMessage().contains("expected NodeEvent"));
    }
  }

  @Test
  public void readAndWriteCommentsBlockStyle() {
    Yaml yaml = getYaml();
    String content = "url: text # comment breaks it";
    Node node = yaml.compose(new StringReader(content));
    assertEquals(" comment breaks it", extractInlineComment(node));
    StringWriter output = new StringWriter();
    yaml.serialize(node, output);
    assertEquals(content, output.toString().trim());
  }
}
