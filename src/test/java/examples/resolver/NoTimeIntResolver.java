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
package examples.resolver;

import java.util.regex.Pattern;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

/**
 * resolve integers without the 12:00:00 pattern
 */
public class NoTimeIntResolver extends Resolver {

  public static final Pattern SIMPLE_INT = Pattern
      .compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+)$");

  /*
   * resolve boolean for only 2 values: true and false
   */
  protected void addImplicitResolvers() {
    addImplicitResolver(Tag.BOOL, BOOL, "tf");
    // define simple int pattern
    addImplicitResolver(Tag.INT, SIMPLE_INT, "-+0123456789");
    addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
    addImplicitResolver(Tag.MERGE, MERGE, "<");
    addImplicitResolver(Tag.NULL, NULL, "~nN\0");
    addImplicitResolver(Tag.NULL, EMPTY, null);
    // addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
  }
}
