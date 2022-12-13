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
package org.yaml.snakeyaml.comments;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.CommentEvent;

/**
 * A comment line. It may be a block comment, blank line, or inline comment.
 */
public class CommentLine {

  private final Mark startMark;
  private final Mark endMark;
  private final String value;
  private final CommentType commentType;

  /**
   * Create
   *
   * @param event - the source
   */
  public CommentLine(CommentEvent event) {
    this(event.getStartMark(), event.getEndMark(), event.getValue(), event.getCommentType());
  }

  /**
   * Create
   *
   * @param startMark - start
   * @param endMark - end
   * @param value - text
   * @param commentType - kind
   */
  public CommentLine(Mark startMark, Mark endMark, String value, CommentType commentType) {
    this.startMark = startMark;
    this.endMark = endMark;
    this.value = value;
    this.commentType = commentType;
  }

  /**
   * getter
   *
   * @return end
   */
  public Mark getEndMark() {
    return endMark;
  }

  /**
   * getter
   *
   * @return start
   */
  public Mark getStartMark() {
    return startMark;
  }

  /**
   * Getter
   *
   * @return kind
   */
  public CommentType getCommentType() {
    return commentType;
  }

  /**
   * Value of this comment.
   *
   * @return comment's value.
   */
  public String getValue() {
    return value;
  }

  public String toString() {
    return "<" + this.getClass().getName() + " (type=" + getCommentType() + ", value=" + getValue()
        + ")>";
  }
}
