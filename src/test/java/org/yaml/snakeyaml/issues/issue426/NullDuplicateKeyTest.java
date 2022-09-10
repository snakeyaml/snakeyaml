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
package org.yaml.snakeyaml.issues.issue426;

import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

public class NullDuplicateKeyTest extends TestCase {

  public void testDuplicateKeyIsAllowed() {
    Yaml loader = new Yaml();
    Map<String, String> duplicateMap = loader.load("~: foo\n" + "~: bar");
    assertEquals(1, duplicateMap.size());
    assertEquals("bar", duplicateMap.get(null));
  }

  public void testDuplicateKeyIsNotAlowed() {
    LoaderOptions options = new LoaderOptions();
    options.setAllowDuplicateKeys(false);
    Yaml loader = new Yaml(options);
    try {
      loader.load("~: foo\n~: bar");
    } catch (DuplicateKeyException e) {
      assertTrue(e.getMessage(), e.getMessage().contains("found duplicate key null"));
    }
  }
}
