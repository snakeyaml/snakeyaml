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
package org.yaml.snakeyaml.issues.issue1080;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import static org.junit.Assert.assertEquals;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1080
 */
public class PatternDefinitionTest {
  @Test
  public void testLoadingMap() {
    DumperOptions dumperOptions = new DumperOptions();
    Representer representer = new Representer(dumperOptions);
    LoaderOptions loaderOptions = new LoaderOptions();
    Constructor constructor = new Constructor(loaderOptions);
    try {
      new Yaml(constructor, representer, dumperOptions, loaderOptions, new FixedYamlResolver());
    } catch (Exception e) {
      assertEquals("No pattern provided for Tag=tag:yaml.org,2002:bool", e.getMessage());
    }
  }
}
