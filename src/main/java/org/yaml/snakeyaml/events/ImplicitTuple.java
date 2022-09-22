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
package org.yaml.snakeyaml.events;

/**
 * The implicit flag of a scalar event is a pair of boolean values that indicate if the tag may be
 * omitted when the scalar is emitted in a plain and non-plain style correspondingly.
 *
 * @see <a href="http://pyyaml.org/wiki/PyYAMLDocumentation#Events">Events</a>
 */
public class ImplicitTuple {

  private final boolean plain;
  private final boolean nonPlain;

  /**
   * Create
   *
   * @param plain - true when tag can be omitted in plain
   * @param nonplain - true when tag can be omitted in non-plain
   */
  public ImplicitTuple(boolean plain, boolean nonplain) {
    this.plain = plain;
    this.nonPlain = nonplain;
  }

  /**
   * @return true when tag may be omitted when the scalar is emitted in a plain style.
   */
  public boolean canOmitTagInPlainScalar() {
    return plain;
  }

  /**
   * @return true when tag may be omitted when the scalar is emitted in a non-plain style.
   */
  public boolean canOmitTagInNonPlainScalar() {
    return nonPlain;
  }

  /**
   * getter
   *
   * @return true when both are false
   */
  public boolean bothFalse() {
    return !plain && !nonPlain;
  }

  @Override
  public String toString() {
    return "implicit=[" + plain + ", " + nonPlain + "]";
  }
}
