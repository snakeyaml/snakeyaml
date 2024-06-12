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
package org.yaml.snakeyaml.issues.issue1091;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1091/malformedinputexception-input-length-1
 * anchors and aliases
 */
public class FileEncodingTest {

  @Test
  public void testReadingFile() {
    Yaml yaml = new Yaml();
    String origin = Util.getLocalResource("issues/issue1091-input.yaml");
    Object parsed = yaml.load(origin);
    assertEquals(1, 1);
  }

  @Test
  public void testReadingFileAsString() {
    Yaml yaml = new Yaml();
    String origin = Util.getLocalResource("issues/issue1091-input.yaml");
    try {
      yaml.loadAs(origin, String.class);
      fail();
    } catch (Exception e) {
      assertTrue(e.getMessage(), e.getMessage().contains("Cannot create property=heading"));
    }
  }
}

