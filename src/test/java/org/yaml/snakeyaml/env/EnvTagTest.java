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
package org.yaml.snakeyaml.env;

import static org.yaml.snakeyaml.env.EnvScalarConstructor.ENV_FORMAT;

import java.io.StringReader;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;

/**
 * test that implicit resolver assigns the tag
 */
public class EnvTagTest extends TestCase {

  public void testImplicitResolverForEnvConstructor() {
    Yaml yaml = new Yaml();
    yaml.addImplicitResolver(EnvScalarConstructor.ENV_TAG, ENV_FORMAT, "$");
    Node loaded = yaml.compose(new StringReader("${PATH}"));
    assertEquals(EnvScalarConstructor.ENV_TAG, loaded.getTag());
  }
}
