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
package org.yaml.snakeyaml.issues.issue1110;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import org.junit.Test;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class ParseJsonTest {

  /**
   * <a href=
   * "https://bitbucket.org/snakeyaml/snakeyaml/issues/1110/exception-during-parse-of-tab-idented-json">cf-app</a>
   */
  @Test
  public void testJsonWithTabs() {
    String str = Util.getLocalResource("issues/issue1110-mtad.json");
    Yaml yaml = new Yaml();
    Map<String, Object> obj = (Map<String, Object>) yaml.load(str);
    assertEquals(4, obj.size());
    assertTrue(obj.containsKey("_schema-version"));
  }

  @Test
  public void testJsonWithTabsSmall() {
    String str = Util.getLocalResource("issues/issue1110-leading-tab.json");
    Yaml yaml = new Yaml();
    Map<String, Object> obj = (Map<String, Object>) yaml.load(str);
    assertEquals(3, obj.size());
    assertTrue(obj.containsKey("modules"));
  }
}
