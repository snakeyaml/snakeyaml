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
package org.yaml.snakeyaml.tokens;

import java.util.List;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Directive Token
 *
 * @param <T> it is either Integer for the YAML directive or String for the TAG directive
 */
public final class DirectiveToken<T> extends Token {

  private final String name;
  private final List<T> value;

  /**
   * Create
   *
   * @param name - directive name
   * @param value - directive value
   * @param startMark - start
   * @param endMark - end
   */
  public DirectiveToken(String name, List<T> value, Mark startMark, Mark endMark) {
    super(startMark, endMark);
    this.name = name;
    if (value != null && value.size() != 2) {
      throw new YAMLException("Two strings must be provided instead of " + value.size());
    }
    this.value = value;
  }

  /**
   * getter
   *
   * @return name
   */
  public String getName() {
    return this.name;
  }

  /**
   * getter
   *
   * @return value
   */
  public List<T> getValue() {
    return this.value;
  }

  /**
   * getter
   *
   * @return its identity
   */
  @Override
  public Token.ID getTokenId() {
    return ID.Directive;
  }
}
