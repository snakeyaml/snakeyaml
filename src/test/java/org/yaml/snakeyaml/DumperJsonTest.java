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
package org.yaml.snakeyaml;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1084/dump-as-json
 */
public class DumperJsonTest extends TestCase {

  private Yaml createYaml() {
    DumperOptions options = new DumperOptions();
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
    options.setDefaultScalarStyle(DumperOptions.ScalarStyle.JSON_SCALAR_STYLE);
    return new Yaml(options);
  }

  public void testJsonStr() {
    assertEquals("\"bar1\"\n", createYaml().dump("bar1"));
  }

  public void testJsonInt() {
    assertEquals("17\n", createYaml().dump(17));
  }

  public void testJsonIntAsStr() {
    assertEquals("\"17\"\n", createYaml().dump("17"));
  }

  public void testJsonIntInCollection() {
    List<Object> list = new ArrayList<Object>();
    list.add(17);
    list.add("17");
    assertEquals("[17, \"17\"]\n", createYaml().dump(list));
  }

  public void testJsonBoolean() {
    assertEquals("true\n", createYaml().dump(true));
  }

  public void testJsonBooleanInCollection() {
    List<Object> list = new ArrayList<Object>();
    list.add(true);
    list.add("true");
    assertEquals("[true, \"true\"]\n", createYaml().dump(list));
  }

  public void testJsonNull() {
    assertEquals("null\n", createYaml().dump(null));
  }

  public void testJsonNullInCollection() {
    List<Object> list = new ArrayList<Object>();
    list.add(null);
    list.add("null");
    assertEquals("[null, \"null\"]\n", createYaml().dump(list));
  }

  /**
   * https://bitbucket.org/snakeyaml/snakeyaml/issues/1084/dump-as-json
   */
  public void testJson() {
    Yaml yaml = createYaml();

    List<Object> list = new ArrayList<Object>();
    list.add(17);
    list.add("foo");
    list.add(true);
    list.add("true");
    list.add(false);
    list.add("false");
    list.add(null);
    list.add("null");

    assertEquals("[17, \"foo\", true, \"true\", false, \"false\", null, \"null\"]\n",
        yaml.dump(list));
  }
}
