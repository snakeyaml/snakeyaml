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
package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom data structure to support compact notation
 * https://bitbucket.org/snakeyaml/snakeyaml/wiki/CompactObjectNotation
 */
public class CompactData {

  private final String prefix;
  private final List<String> arguments = new ArrayList<String>();
  private final Map<String, String> properties = new HashMap<String, String>();

  /**
   * Create
   *
   * @param prefix - by default is serves as a full class Name, but it can be changed
   */
  public CompactData(String prefix) {
    this.prefix = prefix;
  }

  /**
   * getter
   *
   * @return prefix from the document
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Getter
   *
   * @return properties
   */
  public Map<String, String> getProperties() {
    return properties;
  }

  /**
   * getter
   *
   * @return arguments
   */
  public List<String> getArguments() {
    return arguments;
  }

  /**
   * visual representation
   *
   * @return readable data
   */
  @Override
  public String toString() {
    return "CompactData: " + prefix + " " + properties;
  }
}
