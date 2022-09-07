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
package org.yaml.snakeyaml.issues.issue520;

import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class ChompingTest extends TestCase {

  public void testChomp() {
    String input = "description: |+\n" + "  line\n\n";
    Yaml yaml = new Yaml();
    Map<String, String> obj = yaml.load(input);
    assertEquals("line\n\n", obj.get("description"));

    String output = yaml.dump(obj);
    assertEquals(input, output);
  }
}
