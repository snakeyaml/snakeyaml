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

import org.yaml.snakeyaml.error.Mark;

/**
 * Indicate mapping with the same key
 */
public class DuplicateKeyException extends ConstructorException {

  /**
   * Create
   *
   * @param contextMark - context location
   * @param key - the key which is not unique
   * @param problemMark - the problem location
   */
  protected DuplicateKeyException(Mark contextMark, Object key, Mark problemMark) {
    super("while constructing a mapping", contextMark, "found duplicate key " + key, problemMark);
  }
}
