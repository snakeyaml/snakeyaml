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

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;

/**
 * Marks a scalar value.
 */
public final class ScalarEvent extends NodeEvent {

  private final String tag;
  // style flag of a scalar event indicates the style of the scalar.
  private final DumperOptions.ScalarStyle style;
  private final String value;
  // The implicit flag of a scalar event is a pair of boolean values that
  // indicate if the tag may be omitted when the scalar is emitted in a plain
  // and non-plain style correspondingly.
  private final ImplicitTuple implicit;

  public ScalarEvent(String anchor, String tag, ImplicitTuple implicit, String value,
      Mark startMark, Mark endMark, DumperOptions.ScalarStyle style) {
    super(anchor, startMark, endMark);
    this.tag = tag;
    this.implicit = implicit;
    if (value == null) {
      throw new NullPointerException("Value must be provided.");
    }
    this.value = value;
    if (style == null) {
      throw new NullPointerException("Style must be provided.");
    }
    this.style = style;
  }

  /**
   * Tag of this scalar.
   *
   * @return The tag of this scalar, or <code>null</code> if no explicit tag is available.
   */
  public String getTag() {
    return this.tag;
  }

  /**
   * Style of the scalar.
   * <dl>
   * <dt>null</dt>
   * <dd>Flow Style - Plain</dd>
   * <dt>'\''</dt>
   * <dd>Flow Style - Single-Quoted</dd>
   * <dt>'"'</dt>
   * <dd>Flow Style - Double-Quoted</dd>
   * <dt>'|'</dt>
   * <dd>Block Style - Literal</dd>
   * <dt>'&gt;'</dt>
   * <dd>Block Style - Folded</dd>
   * </dl>
   *
   * @see <a href="http://yaml.org/spec/1.1/#id864487">Kind/Style Combinations</a>
   * @return Style of the scalar.
   */
  public DumperOptions.ScalarStyle getScalarStyle() {
    return this.style;
  }

  /**
   * String representation of the value.
   * <p>
   * Without quotes and escaping.
   * </p>
   *
   * @return Value as Unicode string.
   */
  public String getValue() {
    return this.value;
  }

  public ImplicitTuple getImplicit() {
    return this.implicit;
  }

  @Override
  protected String getArguments() {
    return super.getArguments() + ", tag=" + tag + ", style=" + style + "," + implicit + ", value="
        + value;
  }

  @Override
  public Event.ID getEventId() {
    return ID.Scalar;
  }

  public boolean isPlain() {
    return style == DumperOptions.ScalarStyle.PLAIN;
  }

  public boolean isLiteral() {
    return style == DumperOptions.ScalarStyle.LITERAL;
  }

  public boolean isSQuoted() {
    return style == DumperOptions.ScalarStyle.SINGLE_QUOTED;
  }

  public boolean isDQuoted() {
    return style == DumperOptions.ScalarStyle.DOUBLE_QUOTED;
  }

  public boolean isFolded() {
    return style == DumperOptions.ScalarStyle.FOLDED;
  }

  public boolean isJson() {
    return style == DumperOptions.ScalarStyle.JSON_SCALAR_STYLE;
  }
}
