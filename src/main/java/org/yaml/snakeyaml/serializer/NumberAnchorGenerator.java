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
package org.yaml.snakeyaml.serializer;

import java.text.NumberFormat;
import org.yaml.snakeyaml.nodes.Node;

public class NumberAnchorGenerator implements AnchorGenerator {

  private int lastAnchorId = 0;

  public NumberAnchorGenerator(int lastAnchorId) {
    this.lastAnchorId = lastAnchorId;
  }

  /**
   * Create the anchor name or keep the one when it was already created in the node by the low level
   * API
   *
   * @param node - the data to anchor
   * @return unique anchor name or existing anchor name
   */
  public String nextAnchor(Node node) {
    if (node.getAnchor() != null) {
      // keep the anchor when it is set explicitly
      return node.getAnchor();
    }
    this.lastAnchorId++;
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMinimumIntegerDigits(3);
    format.setMaximumFractionDigits(0);// issue 172
    format.setGroupingUsed(false);
    String anchorId = format.format(this.lastAnchorId);
    return "id" + anchorId;
  }
}
