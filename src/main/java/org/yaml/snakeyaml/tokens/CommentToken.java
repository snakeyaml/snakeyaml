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
package org.yaml.snakeyaml.tokens;

import java.util.Objects;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.error.Mark;

/**
 * Comment for humans
 */
public final class CommentToken extends Token {

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
  public CommentToken(CommentType type, String value, Mark startMark, Mark endMark) {
    super(startMark, endMark);
    Objects.requireNonNull(type);
    this.type = type;
    Objects.requireNonNull(value);
    this.value = value;
  }

  /**
   * getter
   *
   * @return the kind
   */
  public CommentType getCommentType() {
    return this.type;
  }

  /**
   * getter
   *
   * @return text
   */
  public String getValue() {
    return this.value;
  }

  @Override
  public Token.ID getTokenId() {
    return ID.Comment;
  }
}
