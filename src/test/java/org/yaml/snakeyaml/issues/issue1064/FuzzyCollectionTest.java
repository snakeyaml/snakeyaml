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
package org.yaml.snakeyaml.issues.issue1064;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class FuzzyCollectionTest {

  /**
   * https://bitbucket.org/snakeyaml/snakeyaml/issues/1064
   */
  @Test
  public void testNoOutOfMemoryError() {
    String datastring =
        " ? - - ? - - ? ? - - ? ? ? - - ? ? - - ? ? ? - - ? ? - ? ? - - ? - - ? ? ? - - ? ? - ?  -? - ? ? - - ? - - ? ? ? - - ? ? - ?  -? - ? ? - - ? - ";
    try (InputStream datastream = new ByteArrayInputStream(datastring.getBytes());
        InputStreamReader reader = new InputStreamReader(datastream, StandardCharsets.UTF_8)) {

      Yaml yaml = new Yaml();
      yaml.loadAs(reader, String.class);
      fail("Should not be OutOfMemoryError");
    } catch (Exception e) {
      assertTrue(e.getMessage(), e.getMessage().contains("Keys must be scalars but found"));
    }
  }
}
