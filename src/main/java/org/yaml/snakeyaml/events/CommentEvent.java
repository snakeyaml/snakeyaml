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

import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.error.Mark;

/**
 * Marks a comment block value.
 */
public final class CommentEvent extends Event {

  private final CommentType type;
  private final String value;

  /**
   * Create
   *
   * @param type - kind
   * @param value - text
   * @param startMark - start
   * @param endMark - end
   */
  public CommentEvent(CommentType type, String value, Mark startMark, Mark endMark) {
    super(startMark, endMark);
    if (type == null) {
      throw new NullPointerException("Event Type must be provided.");
    }
    this.type = type;
    if (value == null) {
      throw new NullPointerException("Value must be provided.");
    }
    this.value = value;
  }

  /**
   * String representation of the value.
   * <p>
   * Without quotes and escaping.
   * </p>
   *
   * @return Value a comment line string without the leading '#' or a blank line.
   */
  public String getValue() {
    return this.value;
  }

  /**
   * The comment type.
   *
   * @return the commentType.
   */
  public CommentType getCommentType() {
    return this.type;
  }

  @Override
  protected String getArguments() {
    return super.getArguments() + "type=" + type + ", value=" + value;
  }

  @Override
  public Event.ID getEventId() {
    return ID.Comment;
  }

}
