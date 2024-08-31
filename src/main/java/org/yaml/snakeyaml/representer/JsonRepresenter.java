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
package org.yaml.snakeyaml.representer;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.Base64;
import java.util.Date;

/**
 * Represent data structures into Nodes in the way suitable for serialisation as JSON
 */
public class JsonRepresenter extends Representer {
  public JsonRepresenter(DumperOptions options) {
    super(options);
    this.representers.put(byte[].class, new RepresentByteArray());
    this.multiRepresenters.put(Date.class, new RepresentDate());
    if (options.getDefaultScalarStyle() != DumperOptions.ScalarStyle.JSON_SCALAR_STYLE) {
      throw new IllegalStateException("JSON requires ScalarStyle.JSON_SCALAR_STYLE");
    }
    if (options.getNonPrintableStyle() != DumperOptions.NonPrintableStyle.ESCAPE) {
      throw new IllegalStateException("JSON requires NonPrintableStyle.ESCAPE");
    }
  }

  /**
   * Use the provided serialisation but emit as string
   */
  protected class RepresentDate extends Representer.RepresentDate {
    @Override
    public Tag getDefaultTag() {
      return Tag.STR;
    }
  }

  /**
   * JSON does not have support for binary data. This method should be overridden to emit the
   * expected string for binary data
   */
  protected class RepresentByteArray implements Represent {
    public Node representData(Object data) {
      String binary = Base64.getEncoder().encodeToString((byte[]) data);
      return representScalar(Tag.STR, binary);
    }
  }
}
