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
package org.yaml.snakeyaml.issues.issue474;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class ByteParseTest extends TestCase {

  public void testParseBytes() {
    Yaml yamlProcessor = new Yaml();
    Byte[] lb = yamlProcessor.loadAs("[0x01,0x02,0xff,0x7f_ee_00_11]", Byte[].class);

    assertEquals(4, lb.length);
    assertEquals(Byte.valueOf((byte) 1), lb[0]);
    assertEquals(Byte.valueOf((byte) 2), lb[1]);
    assertEquals(Byte.valueOf((byte) -1), lb[2]);
    assertEquals(Byte.valueOf((byte) 255), lb[2]); // narrow
    assertEquals(Byte.valueOf((byte) 17), lb[3]);
  }

  public void testParseShorts() {
    Yaml yamlProcessor = new Yaml();
    Short[] lb = yamlProcessor.loadAs("[0x0102,0x7ffe,33000,0x8fff,65000]", Short[].class);

    assertEquals(5, lb.length);
    assertEquals(Short.valueOf((short) 258), lb[0]);
    assertEquals(Short.valueOf((short) 32766), lb[1]);
    assertEquals(Short.valueOf((short) -32536), lb[2]);
    assertEquals(Short.valueOf((short) -28673), lb[3]);
    assertEquals(Short.valueOf((short) -536), lb[4]);
  }
}
