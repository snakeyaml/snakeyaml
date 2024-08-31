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
  public void testSingleLetter() {
    String src = "zNear: 2";

    Yaml yaml = new Yaml();
    try {
      BeanSingleZ camera = yaml.loadAs(src, BeanSingleZ.class);
      fail("Should not fail - but the property is detected as ZNear");
      assertEquals(camera.getZNear(), 2);
    } catch (Exception e) {
      // it looks like Java's Introspector.getBeanInfo(type).getPropertyDescriptors() returns
      // confusing name - ZNear
      assertTrue(e.getMessage().contains("Unable to find property 'zNear' on class"));
    }
  }

  @Test
  public void testDoubleLetter() {
    String src = "zzNear: 2";

    Yaml yaml = new Yaml();
    BeanDoubleZ camera = yaml.loadAs(src, BeanDoubleZ.class);
    assertEquals(camera.getZzNear(), 2);
  }

  public static class BeanSingleZ {
    private int zNear;

    public int getZNear() {
      return zNear;
    }

    public void setZNear(int zNear) {
      this.zNear = zNear;
    }
  }

  public static class BeanDoubleZ {
    private int zzNear;

    public int getZzNear() {
      return zzNear;
    }

    public void setZzNear(int zNear) {
      this.zzNear = zNear;
    }
  }
}

