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
package org.yaml.snakeyaml.issues.issue529;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

// StringIndexOutOfBoundsException [OSS-Fuzz 47028]
public class Fuzzy47028Test {

  @Test
  public void parseEmptyFloat_47028() {
    try {
      LoaderOptions options = new LoaderOptions();
      Yaml yaml = new Yaml(options);
      yaml.load("- !!float");
      fail("Should report invalid YAML");
    } catch (YAMLException e) {
      assertTrue(e.getMessage().contains("while constructing a float"));
      assertTrue(e.getMessage().contains("found empty value"));
    }
  }

  @Test
  public void parseEmptyInt_47028() {
    try {
      LoaderOptions options = new LoaderOptions();
      Yaml yaml = new Yaml(options);
      yaml.load("- !!int");
      fail("Should report invalid YAML");
    } catch (YAMLException e) {
      assertTrue(e.getMessage().contains("while constructing an int"));
      assertTrue(e.getMessage().contains("found empty value"));
    }
  }
}
