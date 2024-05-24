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

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1088/error-when-trying-to-load-an-object-with
 */
public class YamlLombokTest {

  @Test
  public void testLowerCase() {
    String src = "zNear: 0.2\nzfar: 120\nname: foo";

    Yaml yaml = new Yaml();
    try {
      Camera camera = yaml.loadAs(src, Camera.class);
      fail("Case ?");

      assertEquals(camera.getZNear(), 0.2f);
      assertEquals(camera.getZFar(), 120f);
    } catch (Exception e) {
      // TODO lombok & case ?
    }
  }

  @Test
  public void testUpperCase() {
    String src = "ZNear: 0.2\nZFar: 120\nname: foo";

    Yaml yaml = new Yaml();
    Camera camera = yaml.loadAs(src, Camera.class);

    assertEquals(camera.getZNear(), 0.2f, 0.00001);
    assertEquals(camera.getZFar(), 120f, 0.00001);
    assertEquals(camera.getName(), "foo");
  }

  @Test
  public void testCameraObscure() {
    CameraObscure obscure = new CameraObscure();
    obscure.setZNear("foo"); // where is the second field ?
    Yaml yaml = new Yaml();
    CameraObscure camera = yaml.loadAs("ZNear: 17", CameraObscure.class);

    assertEquals(camera.getZNear(), "17");
  }

  @Getter
  @Setter
  public static class Camera {
    private float zNear;
    private float zFar;
    private String name;
  }

  @Getter
  @Setter
  public static class CameraObscure {
    // changing the order affects the Lombok magic (only the first one is respected)
    // the case of the first letter is ignored
    private String zNear;
    private int ZNear;
  }
}
