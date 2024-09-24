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
package org.yaml.snakeyaml.issues.issue377;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test issue 54 for SnakeYAML Engine (no space is needed after an alias)
 */
public class SpaceAfterAliasTest {
  @Test
  public void testNoSpaceIsRequired() {
    HashMap<Object, Boolean> map = new HashMap<>();
    map.put(":one", true);
    map.put(map, true);
    DumperOptions dumperOptions = new DumperOptions();
    LoaderOptions loaderOptions = new LoaderOptions();
    loaderOptions.setAllowRecursiveKeys(true);
    Yaml yaml = new Yaml(loaderOptions);
    String output = yaml.dump(map);
    assertEquals("&id001\n" + ":one: true\n" + "*id001: true\n", output);
    Object parsed = yaml.load(output);
    assertNotNull(parsed);
  }
}
