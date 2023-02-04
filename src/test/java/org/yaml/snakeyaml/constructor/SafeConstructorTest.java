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
package org.yaml.snakeyaml.constructor;

import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlCreator;

public class SafeConstructorTest extends TestCase {

  public void testConstructFloat() {
    Yaml yaml = new Yaml();
    assertEquals(3.1416, yaml.load("+3.1416"));
    assertEquals(Double.POSITIVE_INFINITY, yaml.load("+.inf"));
    assertEquals(Double.POSITIVE_INFINITY, yaml.load(".inf"));
    assertEquals(Double.NEGATIVE_INFINITY, yaml.load("-.inf"));
  }

  public void testSafeConstructUntrusted() {
    Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
    assertEquals(3.1416, yaml.load("+3.1416"));
  }

  public void testSafeConstructTrusted() {
    Yaml yaml = new Yaml(new SafeConstructor(YamlCreator.trustedLoaderOptions()));
    assertEquals(3.1416, yaml.load("+3.1416"));
  }

  public void testSafeConstructJavaBeanGlobalTag() {
    Yaml yaml = new Yaml(new SafeConstructor(YamlCreator.trustedLoaderOptions()));
    String data = "--- !!org.yaml.snakeyaml.constructor.Person\nfirstName: Andrey\nage: 99";
    try {
      yaml.load(data);
      fail("JavaBeans cannot be created by SafeConstructor with a global tag.");
    } catch (ConstructorException e) {
      assertTrue(e.getMessage().contains(
          "could not determine a constructor for the tag tag:yaml.org,2002:org.yaml.snakeyaml.constructor.Person"));
    }
  }

  public void testSafeConstructMap() {
    Yaml yaml = new Yaml(new SafeConstructor(YamlCreator.trustedLoaderOptions()));
    String data = "---\nfirstName: Andrey\nage: 99";
    try {
      yaml.loadAs(data, Map.class);
      fail("loadAs() should not work");
    } catch (ConstructorException e) {
      assertTrue(e.getMessage(), e.getMessage().contains(
          "could not determine a constructor for the tag tag:yaml.org,2002:java.util.Map"));
    }
  }

  public void testSafeConstructPerson() {
    Yaml yaml = new Yaml(new SafeConstructor(YamlCreator.trustedLoaderOptions()));
    String data = "---\nfirstName: Andrey\nage: 99";
    try {
      yaml.loadAs(data, Person.class);
      fail("JavaBeans cannot be created by SafeConstructor even without global tag with loadAs()");
    } catch (ConstructorException e) {
      String error = e.getMessage();
      assertTrue(error, error.contains(
          "could not determine a constructor for the tag tag:yaml.org,2002:org.yaml.snakeyaml.constructor.Person"));
    }
  }
}
