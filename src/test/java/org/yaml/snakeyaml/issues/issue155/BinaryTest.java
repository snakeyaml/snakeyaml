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
package org.yaml.snakeyaml.issues.issue155;

import java.nio.charset.StandardCharsets;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class BinaryTest extends TestCase {

  public void testBinaryString() throws Exception {
    String data = "\u2666";
    byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
    String inconsistentString = new String(bytes, StandardCharsets.ISO_8859_1);
    Yaml yaml = new Yaml();
    String payload = yaml.dump(inconsistentString);
    // System.out.println("payload: '" + payload + "'");
    String loaded = new String((byte[]) yaml.load(payload), StandardCharsets.UTF_8);
    assertEquals(inconsistentString, loaded);
  }
}
