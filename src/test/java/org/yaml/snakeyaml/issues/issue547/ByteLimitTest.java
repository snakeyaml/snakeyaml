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
package org.yaml.snakeyaml.issues.issue547;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

public class ByteLimitTest {

  @Test
  public void testSetCodePointLimit() {
    LoaderOptions options = new LoaderOptions();
    options.setCodePointLimit(15);
    Yaml yaml = new Yaml(options);
    try {
      yaml.load("12345678901234567890");
      fail("Long input should not be accepted");
    } catch (Exception e) {
      assertEquals("The incoming YAML document exceeds the limit: 15 code points.", e.getMessage());
    }
  }

  @Test
  public void testLoadAll553() {
    LoaderOptions options = new LoaderOptions();
    options.setCodePointLimit(15);
    Yaml yaml = new Yaml(options);
    try {
      Iterator<Object> iter = yaml.loadAll("12345678901234567890").iterator();
      iter.next();
      fail("Long input should not be accepted for loadAll");
    } catch (Exception e) {
      assertEquals("The incoming YAML document exceeds the limit: 15 code points.", e.getMessage());
    }
  }
}
