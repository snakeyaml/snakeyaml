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
package org.yaml.snakeyaml.issues.issue543;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

// Stackoverflow [OSS-Fuzz - 50355]
public class Fuzzer50355Test {

  @Test
  public void parse_50355() {
    LoaderOptions options = new LoaderOptions();
    options.setAllowRecursiveKeys(false);
    Yaml yaml = new Yaml(options);
    String strYaml = Util.getLocalResource("fuzzer/YamlFuzzer-5167495132086272");
    try {
      yaml.load(strYaml);
      fail("Recursive keys should not be accepted");
    } catch (YAMLException e) {
      assertEquals("Recursive key for mapping is detected but it is not configured to be allowed.",
          e.getMessage());
    }
  }
}
