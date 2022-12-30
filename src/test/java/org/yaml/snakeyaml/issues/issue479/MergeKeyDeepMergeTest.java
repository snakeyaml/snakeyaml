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
package org.yaml.snakeyaml.issues.issue479;

import java.io.InputStream;
import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class MergeKeyDeepMergeTest extends TestCase {

  public void testOnlyTheCurrentMappingIsMerged() {
    Constructor constructor = new Constructor(DemoProperty.class, new LoaderOptions());
    Representer representer = new Representer(new DumperOptions());
    representer.getPropertyUtils().setSkipMissingProperties(true);
    Yaml yaml = new Yaml(constructor, representer);

    InputStream inputStream =
        MergeKeyDeepMergeTest.class.getResourceAsStream("/issues/issue479.yaml");
    DemoProperty property = yaml.load(inputStream);
    assertEquals("2", property.getSystem().get("erp").getTest1());
    assertNull(property.getSystem().get("erp").getMysql().getDb_host());
    assertNull(property.getSystem().get("erp").getMysql().getDb_port());
    assertEquals("erp", property.getSystem().get("erp").getMysql().getDb_name());
    assertNull(property.getSystem().get("erp").getMysql().getDb_user());
    assertNull(property.getSystem().get("erp").getMysql().getDb_password());
  }

  public void testMergeKeyDeepMerge() {
    Yaml yaml = new Yaml();

    InputStream inputStream =
        MergeKeyDeepMergeTest.class.getResourceAsStream("/issues/issue479.yaml");
    Object property = yaml.load(inputStream);
    String output = yaml.dump(property);
    // System.out.println(output);
    String expected = Util.getLocalResource("issues/issue479-output.yaml");
    assertEquals(expected, output);
  }

  public void testMergeAsJavabean() {
    Constructor constructor = new Constructor(DemoProperty.class, new LoaderOptions());
    Representer representer = new Representer(new DumperOptions());
    representer.getPropertyUtils().setSkipMissingProperties(true);
    Yaml yaml = new Yaml(constructor, representer);

    InputStream inputStream =
        MergeKeyDeepMergeTest.class.getResourceAsStream("/issues/issue479-1.yaml");
    DemoProperty property = yaml.load(inputStream);
    String output = yaml.dump(property);
    // System.out.println(output);

    String v = "!!org.yaml.snakeyaml.issues.issue479.DemoProperty\n" + "system:\n" + "  erp:\n"
        + "    mysql: {db_host: mysql.avatar2.test, db_name: erp, db_password: secret, db_port: 3306,\n"
        + "      db_user: default}\n" + "    test1: '2'\n";
    assertEquals(v, output);

    assertEquals("2", property.getSystem().get("erp").getTest1());
    assertEquals("mysql.avatar2.test", property.getSystem().get("erp").getMysql().getDb_host());
    assertEquals(3306, (int) property.getSystem().get("erp").getMysql().getDb_port());
    assertEquals("erp", property.getSystem().get("erp").getMysql().getDb_name());
    assertEquals("default", property.getSystem().get("erp").getMysql().getDb_user());
    assertEquals("secret", property.getSystem().get("erp").getMysql().getDb_password());
  }
}
