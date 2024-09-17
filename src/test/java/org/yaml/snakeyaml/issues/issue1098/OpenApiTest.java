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
package org.yaml.snakeyaml.issues.issue1098;

import org.junit.Test;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.ReaderException;

import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1098
 */
public class OpenApiTest {

  @Test
  public void loadAsInputStream() {
    InputStream str = Util.getInputstream("issues/issue1098-openapi.yaml");
    Yaml yaml = new Yaml();
    Map<String, Object> sourceTree = yaml.load(str);
    assertEquals(5, sourceTree.size());
  }

  @Test
  public void loadAsString() {
    String str = Util.getLocalResource("issues/issue1098-openapi.yaml");
    Yaml yaml = new Yaml();
    try {
      yaml.load(str);
      fail("Use stream to parse emoji");
    } catch (ReaderException e) {
      assertEquals(3701, e.getPosition());
    }
  }

  @Test
  public void loadSmallAsString() {
    String str = Util.getLocalResource("issues/issue1098-small-openapi.yaml");
    Yaml yaml = new Yaml();
    try {
      yaml.load(str);
      fail("Use stream to parse emoji");
    } catch (ReaderException e) {
      assertEquals(4, e.getPosition());
    }
  }

  @Test
  public void loadSmallAsStream() {
    InputStream str = Util.getInputstream("issues/issue1098-small-openapi.yaml");
    Yaml yaml = new Yaml();
    Map<String, Object> sourceTree = yaml.load(str);
    assertEquals(1, sourceTree.size());
  }
}
