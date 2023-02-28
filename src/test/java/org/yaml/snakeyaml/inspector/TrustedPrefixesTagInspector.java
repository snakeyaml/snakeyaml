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
package org.yaml.snakeyaml.inspector;

import java.util.List;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Allow to create classes with custom global tag if the class name matches any of the provided
 * prefixes.
 */
public final class TrustedPrefixesTagInspector implements TagInspector {

  private final List<String> trustedList;

  /**
   * Create
   *
   * @param trustedList - list of prefixes to allow. It may be the package names
   */
  public TrustedPrefixesTagInspector(List<String> trustedList) {
    this.trustedList = trustedList;
  }

  /**
   * Check
   *
   * @param tag - the global tag to allow
   * @return true when the custom global tag is allowed to create a custom Java instance
   */
  @Override
  public boolean isGlobalTagAllowed(Tag tag) {
    for (String trusted : trustedList) {
      if (tag.getClassName().startsWith(trusted)) {
        return true;
      }
    }
    return false;
  }
}
