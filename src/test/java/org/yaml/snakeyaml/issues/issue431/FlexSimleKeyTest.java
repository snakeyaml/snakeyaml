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
package org.yaml.snakeyaml.issues.issue431;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class FlexSimleKeyTest extends TestCase {

  private final int len = 130;

  public void testLongKey() {
    Yaml dumper = new Yaml(createOptions(len));
    Map<String, Object> root = new HashMap();
    Map<String, String> map = new HashMap<>();
    String key = createKey(len);
    map.put(key, "v1");
    root.put("data", map);
    assertEquals("data: {? '" + key + "'\n  : v1}\n", dumper.dump(root));
  }

  public void testForceLongKeyToBeImplicit() {
    Yaml dumper = new Yaml(createOptions(len + 10));
    Map<String, Object> root = new HashMap();
    Map<String, String> map = new HashMap<>();
    String key = createKey(len);
    map.put(key, "v1");
    root.put("data", map);
    assertEquals("data: {'" + key + "': v1}\n", dumper.dump(root));
  }

  public void testTooLongKeyLength() {
    try {
      createOptions(1024 + 1);
      fail("Length must be restricted to 1024 chars");
    } catch (YAMLException e) {
      assertEquals(
          "The simple key must not span more than 1024 stream characters. See https://yaml.org/spec/1.1/#id934537",
          e.getMessage());
    }
  }

  private DumperOptions createOptions(int len) {
    DumperOptions dumperOptions = new DumperOptions();
    dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.AUTO);
    dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
    dumperOptions.setMaxSimpleKeyLength(len);
    return dumperOptions;
  }

  private String createKey(int length) {
    StringBuffer outputBuffer = new StringBuffer(length);
    for (int i = 0; i < length; i++) {
      outputBuffer.append("" + (i + 1) % 10);
    }
    String prefix = String.valueOf(length);
    String result =
        prefix + "_" + outputBuffer.toString().substring(0, length - prefix.length() - 1);
    if (result.length() != length) {
      throw new RuntimeException("It was: " + result.length());
    }
    return result;
  }
}
