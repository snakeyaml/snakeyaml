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
package org.yaml.snakeyaml.issues.issue205;

import java.io.InputStream;
import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class AppleSmileTest extends TestCase {

  public void testEmoji() {
    // http://support.apple.com/en-us/ht4976
    InputStream input = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("issues/ios_emoji_surrogate.yaml");
    Yaml yaml = new Yaml();
    Map<String, String> map = yaml.load(input);
    String ios_emoji = map.get("text");
    assertEquals("😷😊", ios_emoji);
    System.out.println(ios_emoji);
  }
}
