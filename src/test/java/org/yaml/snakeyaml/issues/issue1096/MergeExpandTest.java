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
package org.yaml.snakeyaml.issues.issue1096;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

public class MergeExpandTest {

  @Test
  public void simple_load_Merge() {
    String str = Util.getLocalResource("issues/issue1096-simple-merge-input.yaml");
    String expected = Util.getLocalResource("issues/issue1096-simple-merge-output.yaml");

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setMergeOnCompose(true);

    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDereferenceAliases(true);

    Yaml yaml = new Yaml(loaderOptions, dumperOptions);
    Node sourceTree = yaml.compose(new StringReader(str));

    StringWriter writer = new StringWriter();
    yaml.serialize(sourceTree, writer);
    String out = writer.toString();

    assertEquals(expected, out);
  }

  @Test
  public void specs_load_Merge() {
    String str = Util.getLocalResource("issues/issue1096-merge-input.yaml");
    String expected = Util.getLocalResource("issues/issue1096-merge-output.yaml");

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setProcessComments(false);
    loaderOptions.setMergeOnCompose(true);

    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDereferenceAliases(true);

    Yaml yaml = new Yaml(loaderOptions, dumperOptions);
    Node sourceTree = yaml.compose(new StringReader(str));

    StringWriter writer = new StringWriter();
    yaml.serialize(sourceTree, writer);
    String out = writer.toString();

    assertEquals(expected, out);
  }

  @Test
  public void merge_As_Scalar() {
    String str =
        "test-list:\n" + " - &1\n" + "   a: 1\n" + "   b: 2\n" + " - &2 <<: *1\n" + " - <<: *2";

    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setProcessComments(false);
    loaderOptions.setMergeOnCompose(true);

    Yaml yaml = new Yaml(loaderOptions);
    try {
      yaml.compose(new StringReader(str));
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Expected mapping node or an anchor referencing mapping"));
    }

  }



}
