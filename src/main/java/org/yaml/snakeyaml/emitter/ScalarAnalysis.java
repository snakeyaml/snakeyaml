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
package org.yaml.snakeyaml.emitter;


/**
 * Accumulate information to choose the scalar style
 */
public final class ScalarAnalysis {

  private final String scalar;
  private final boolean empty;
  private final boolean multiline;
  private final boolean allowFlowPlain;
  private final boolean allowBlockPlain;
  private final boolean allowSingleQuoted;
  private final boolean allowBlock;

  public ScalarAnalysis(String scalar, boolean empty, boolean multiline, boolean allowFlowPlain,
      boolean allowBlockPlain, boolean allowSingleQuoted, boolean allowBlock) {
    this.scalar = scalar;
    this.empty = empty;
    this.multiline = multiline;
    this.allowFlowPlain = allowFlowPlain;
    this.allowBlockPlain = allowBlockPlain;
    this.allowSingleQuoted = allowSingleQuoted;
    this.allowBlock = allowBlock;
  }

  public String getScalar() {
    return scalar;
  }

  public boolean isEmpty() {
    return empty;
  }

  public boolean isMultiline() {
    return multiline;
  }

  public boolean isAllowFlowPlain() {
    return allowFlowPlain;
  }

  public boolean isAllowBlockPlain() {
    return allowBlockPlain;
  }

  public boolean isAllowSingleQuoted() {
    return allowSingleQuoted;
  }

  public boolean isAllowBlock() {
    return allowBlock;
  }
}
