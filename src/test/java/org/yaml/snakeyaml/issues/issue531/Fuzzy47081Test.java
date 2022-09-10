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
package org.yaml.snakeyaml.issues.issue531;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

// Stackoverflow [OSS-Fuzz - 47081]

/**
 * A proof that this issue is a FALSE POSITIVE https://nvd.nist.gov/vuln/detail/CVE-2022-38752
 */
public class Fuzzy47081Test {

  /**
   * Recursive key should NOT be used for untrusted data to avoid
   */
  @Test
  public void parse47081_no_recursion_allowed() {
    LoaderOptions options = new LoaderOptions();
    options.setAllowRecursiveKeys(false); // must be set to false for untrusted source
    Yaml yaml = new Yaml(options);
    String strYaml = "  &a\n" + "- *a\n" + "- *a\n" + "- *a:\n" + "- *a\n" + "- *a\n" + "- *a";
    try {
      yaml.load(strYaml);
      fail("Should report invalid YAML: " + strYaml);
    } catch (YAMLException e) {
      assertEquals("Recursive key for mapping is detected but it is not configured to be allowed.",
          e.getMessage());
    }
  }

  /**
   * Recursive list fails (with StackOverflowError) because it is used as a key Recursive key should
   * NOT be used for untrusted data
   */
  @Test
  public void parse47081_allow_recursion() {
    try {
      LoaderOptions options = new LoaderOptions();
      options.setAllowRecursiveKeys(true);
      Yaml yaml = new Yaml(options);
      String strYaml = "&a\n" + "- *a\n" // if this line is removed, the test properly complains
      // about the recursive keys in map -> Recursive key for
      // mapping is detected, but it is not configured to be
      // allowed.
          + "- *a:\n"; // when the colon is removed, the test is Ok, because the recursive list is
      // not a key
      // System.out.println(strYaml);
      yaml.load(strYaml);
      fail("Should report invalid YAML: " + strYaml);
    } catch (StackOverflowError e) {
      assertTrue(true);
    }
  }

  @Test
  public void parse47081_no_colon() {
    LoaderOptions options = new LoaderOptions();
    options.setAllowRecursiveKeys(true);
    Yaml yaml = new Yaml(options);
    String strYaml = "&a\n" + "- *a\n" + "- *a\n";
    List<Object> parsed = yaml.load(strYaml);
    assertEquals(strYaml, 2, parsed.size());
  }
}
