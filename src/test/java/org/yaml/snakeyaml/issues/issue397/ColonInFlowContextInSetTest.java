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

import java.util.Set;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class ColonInFlowContextInSetTest extends TestCase {

  private final Yaml loader = new Yaml();

  public void testSet() {
    Set<String> set = loader.load("!!set { http://foo }");
    assertTrue(set.contains("http://foo"));
  }

  public void testSetNoSpaces() {
    Set<String> set = loader.load("!!set {http://foo}");
    assertTrue(set.contains("http://foo"));
  }

  public void testSet2() {
    Set<String> set = loader.load("!!set { http://foo,http://bar }");
    assertTrue(set.contains("http://foo"));
    assertTrue(set.contains("http://bar"));
  }
}
