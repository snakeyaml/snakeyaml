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

import org.yaml.snakeyaml.nodes.Tag;

/**
 * TagInspector which does not allow to create any custom instance. It should not be used when the
 * data comes from untrusted source to prevent possible remote code invocation.
 */
public final class UnTrustedTagInspector implements TagInspector {

  /**
   * Allow none
   *
   * @param tag - the global tag to reject
   * @return always return false
   */
  @Override
  public boolean isGlobalTagAllowed(Tag tag) {
    return false;
  }
}
