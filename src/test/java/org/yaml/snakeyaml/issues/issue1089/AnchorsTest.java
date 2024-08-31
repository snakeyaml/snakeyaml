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
package org.yaml.snakeyaml.issues.issue1089;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1089/anchors-are-not-retained scalars do not use
 * anchors and aliases
 */
public class AnchorsTest {

  private Yaml create() {
    LoaderOptions lopts = new LoaderOptions();
    lopts.setProcessComments(true);
    DumperOptions dopts = new DumperOptions();
    dopts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    dopts.setCanonical(false);
    return new Yaml(new Constructor(lopts), new Representer(dopts), dopts, lopts, new Resolver());
  }

  @Test
  public void testNoAnchorForScalar() {
    Yaml yaml = create();
    String origin = Util.getLocalResource("issues/issue1089-input.yaml");
    Map<String, Object> rootInstance = yaml.load(origin);
    Map<String, Object> image1 = (Map<String, Object>) rootInstance.get("image1");
    Map<String, Object> image2 = (Map<String, Object>) rootInstance.get("image2");
    assertNotSame(image1, image2);
    assertEquals(image1, image2);
    Object repo1 = image1.get("repository");
    Object repo2 = image2.get("repository");
    assertSame("Must point to the same instance", repo1, repo2);
    assertEquals(String.class, repo1.getClass()); // String is a scalar, no anchor will be created
    String output = yaml.dump(rootInstance);
    String expected = Util.getLocalResource("issues/issue1089-output.yaml");
    assertEquals(expected, output);
  }

  @Test
  public void testAnchors() {
    Yaml yaml = create();
    String origin = Util.getLocalResource("issues/issue1089-non-scalar-input.yaml");
    Map<String, Object> rootInstance = yaml.load(origin);
    assertSame(rootInstance.get("image1"), rootInstance.get("image2"));
    String output = yaml.dump(rootInstance);
    String expected = Util.getLocalResource("issues/issue1089-non-scalar-output.yaml");
    assertEquals(expected, output);
  }
}

