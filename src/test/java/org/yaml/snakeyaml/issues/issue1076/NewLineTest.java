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
package org.yaml.snakeyaml.issues.issue1076;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertEquals;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1076
 */
public class NewLineTest {
  @Test
  public void doubleQuotedNewLine() {
    String plainStyle = "b\\nc";
    assertEquals(4, plainStyle.length());
    assertEquals('b', plainStyle.charAt(0));
    assertEquals("Single slash is expected", '\\', plainStyle.charAt(1));
    assertEquals('n', plainStyle.charAt(2));
    assertEquals('c', plainStyle.charAt(3));

    Yaml yaml = new Yaml();
    assertEquals(plainStyle, yaml.load(plainStyle));

    String singleQuoted = "'" + plainStyle + "'";
    assertEquals(plainStyle, yaml.load(singleQuoted));

    String doubleQuoted = "\"" + plainStyle + "\"";
    assertEquals("b\nc", yaml.load(doubleQuoted));
  }
}
