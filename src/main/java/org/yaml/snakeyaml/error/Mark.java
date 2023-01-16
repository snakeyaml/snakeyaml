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
package org.yaml.snakeyaml.error;

import java.io.Serializable;
import org.yaml.snakeyaml.scanner.Constant;

/**
 * It's just a record and its only use is producing nice error messages. Parser does not use it for
 * any other purposes.
 */
public final class Mark implements Serializable {

  private final String name;
  private final int index;
  private final int line;
  private final int column;
  private final int[] buffer;
  private final int pointer;

  private static int[] toCodePoints(char[] str) {
    int[] codePoints = new int[Character.codePointCount(str, 0, str.length)];
    for (int i = 0, c = 0; i < str.length; c++) {
      int cp = Character.codePointAt(str, i);
      codePoints[c] = cp;
      i += Character.charCount(cp);
    }
    return codePoints;
  }

  public Mark(String name, int index, int line, int column, char[] str, int pointer) {
    this(name, index, line, column, toCodePoints(str), pointer);
  }

  public Mark(String name, int index, int line, int column, int[] buffer, int pointer) {
    super();
    this.name = name;
    this.index = index;
    this.line = line;
    this.column = column;
    this.buffer = buffer;
    this.pointer = pointer;
  }

  private boolean isLineBreak(int c) {
    return Constant.NULL_OR_LINEBR.has(c);
  }

  public String get_snippet(int indent, int max_length) {
    float half = max_length / 2f - 1f;
    int start = pointer;
    String head = "";
    while ((start > 0) && !isLineBreak(buffer[start - 1])) {
      start -= 1;
      if (pointer - start > half) {
        head = " ... ";
        start += 5;
        break;
      }
    }
    String tail = "";
    int end = pointer;
    while ((end < buffer.length) && !isLineBreak(buffer[end])) {
      end += 1;
      if (end - pointer > half) {
        tail = " ... ";
        end -= 5;
        break;
      }
    }

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < indent; i++) {
      result.append(" ");
    }
    result.append(head);
    for (int i = start; i < end; i++) {
      result.appendCodePoint(buffer[i]);
    }
    result.append(tail);
    result.append("\n");
    for (int i = 0; i < indent + pointer - start + head.length(); i++) {
      result.append(" ");
    }
    result.append("^");
    return result.toString();
  }

  public String get_snippet() {
    return get_snippet(4, 75);
  }

  @Override
  public String toString() {
    String snippet = get_snippet();
    String builder =
        " in " + name + ", line " + (line + 1) + ", column " + (column + 1) + ":\n" + snippet;
    return builder;
  }

  public String getName() {
    return name;
  }

  /**
   * starts with 0
   *
   * @return line number
   */
  public int getLine() {
    return line;
  }

  /**
   * starts with 0
   *
   * @return column number
   */
  public int getColumn() {
    return column;
  }

  /**
   * starts with 0
   *
   * @return character number
   */
  public int getIndex() {
    return index;
  }

  public int[] getBuffer() {
    return buffer;
  }

  public int getPointer() {
    return pointer;
  }
}
