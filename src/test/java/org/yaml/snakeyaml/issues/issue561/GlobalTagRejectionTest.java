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
package org.yaml.snakeyaml.issues.issue561;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class GlobalTagRejectionTest {

  /**
   * https://securitylab.github.com/research/swagger-yaml-parser-vulnerability/
   */
  @Test
  public void testDenyScriptManager() {
    try {
      String malicious = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader "
          + "[[!!java.net.URL [\"http://localhost\"]]]]";
      Yaml yaml = new Yaml();
      yaml.load(malicious);
      fail("ScriptEngineManager should not be accepted");
    } catch (Exception e) {
      assertTrue(e.getMessage(), e.getMessage().startsWith(
          "Global tag is not allowed: tag:yaml.org,2002:javax.script.ScriptEngineManager"));
    }
  }

  @Test
  public void testDenyAnyTag() {
    try {
      String malicious = "!!java.lang.String foo";
      Yaml yaml = new Yaml();
      yaml.load(malicious);
      fail("Global tags are rejected by default");
    } catch (Exception e) {
      assertTrue(e.getMessage(), e.getMessage()
          .startsWith("Global tag is not allowed: tag:yaml.org,2002:java.lang.String"));
    }
  }
}
