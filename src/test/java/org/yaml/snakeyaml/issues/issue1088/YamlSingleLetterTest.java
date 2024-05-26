/**
 * Copyright (c) 2008, SnakeYAML
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.issues.issue1088;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1088/error-when-trying-to-load-an-object-with
 */
public class YamlSingleLetterTest {
  @Test
  public void test() {
    String src = "zNear: 2";

    Yaml yaml = new Yaml();
    try {
      Camera camera = yaml.loadAs(src, Camera.class);
      fail();
      assertEquals(camera.getZNear(), 2);
    } catch (Exception e) {
      // TODO
      assertTrue(e.getMessage().contains("Unable to find property 'zNear' on class"));

    }
  }

  public static class Camera {
    private int zNear;

    public int getZNear() {
      return zNear;
    }

    public void setZNear(int zNear) {
      this.zNear = zNear;
    }
  }
}
