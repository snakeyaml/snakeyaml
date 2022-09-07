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
package org.yaml.snakeyaml.issues.issue397;

import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class ColonInFlowContextInMapTest extends TestCase {

  private final Yaml loader = new Yaml();

  public void test1() {
    Map<String, Integer> map = loader.load("{a: 1}");
    assertEquals(Integer.valueOf(1), map.get("a"));
  }

  public void test2() {
    Map<String, Integer> map = loader.load("{a:}");
    assertTrue(map.containsKey("a"));
  }

  public void test3() {
    Map<String, Integer> map = loader.load("{a}");
    assertTrue(map.containsKey("a"));
  }

  public void testTheOnlyCounterIntuitiveCase() {
    Map<String, Integer> map = loader.load("{a:1}");
    assertTrue(map.containsKey("a:1"));
  }
}
