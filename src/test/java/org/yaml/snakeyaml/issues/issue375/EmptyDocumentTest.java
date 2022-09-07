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
package org.yaml.snakeyaml.issues.issue375;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class EmptyDocumentTest {

  @Test
  public void returnNullForEmptyDocument() {
    Yaml yaml = new Yaml();
    Assert.assertNull(yaml.loadAs("", TestObject.class));
    Assert.assertNull(yaml.loadAs("\n  \n", TestObject.class));
    Assert.assertNull(yaml.loadAs("---\n", TestObject.class));
    Assert.assertNull(yaml.loadAs("---\n#comment\n...\n", TestObject.class));
  }

  public static class TestObject {

    private int attribute1;

    public int getAttribute1() {
      return attribute1;
    }

    public void setAttribute1(int attribute1) {
      this.attribute1 = attribute1;
    }
  }
}
