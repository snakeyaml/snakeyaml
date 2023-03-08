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
package org.yaml.snakeyaml.issues.issue576;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;

/**
 * https://github.com/FasterXML/jackson-dataformats-text/issues/400
 * https://github.com/FasterXML/jackson-dataformats-text/pull/401
 */
public class FuzzYAMLRead50431Test {

  @Test
  public void testIncompleteValue() {
    Yaml yaml = new Yaml();
    try {
      yaml.load("\"\\UE30EEE");
      fail("Invalid escape code in double quoted scalar should not be accepted");
    } catch (ScannerException e) {
      assertTrue(e.getMessage(), e.getMessage().contains("found unknown escape character E30EEE"));
    }
  }

  @Test
  public void testProperValue() {
    Yaml yaml = new Yaml();
    String parsed = yaml.load("\"\\U0000003B\"");
    assertEquals(1, parsed.length());
    assertEquals("\u003B", parsed);
  }

  @Test
  public void testNotQuoted() {
    Yaml yaml = new Yaml();
    String parsed = yaml.load("\\UE30EEE");
    assertEquals(8, parsed.length());
    assertEquals("\\UE30EEE", parsed);
  }
}
