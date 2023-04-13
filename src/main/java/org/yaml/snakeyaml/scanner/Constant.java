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
package org.yaml.snakeyaml.scanner;

import java.util.Arrays;

/**
 * Keep constants
 */
public final class Constant {

  private static final String ALPHA_S =
      "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";

  private static final String LINEBR_S = "\n\u0085\u2028\u2029";
  private static final String FULL_LINEBR_S = "\r" + LINEBR_S;
  private static final String NULL_OR_LINEBR_S = "\0" + FULL_LINEBR_S;
  private static final String NULL_BL_LINEBR_S = " " + NULL_OR_LINEBR_S;
  private static final String NULL_BL_T_LINEBR_S = "\t" + NULL_BL_LINEBR_S;
  private static final String NULL_BL_T_S = "\0 \t";
  private static final String URI_CHARS_S = ALPHA_S + "-;/?:@&=+$,_.!~*'()[]%";

  public static final Constant LINEBR = new Constant(LINEBR_S);
  public static final Constant NULL_OR_LINEBR = new Constant(NULL_OR_LINEBR_S);
  public static final Constant NULL_BL_LINEBR = new Constant(NULL_BL_LINEBR_S);
  public static final Constant NULL_BL_T_LINEBR = new Constant(NULL_BL_T_LINEBR_S);
  public static final Constant NULL_BL_T = new Constant(NULL_BL_T_S);
  public static final Constant URI_CHARS = new Constant(URI_CHARS_S);

  public static final Constant ALPHA = new Constant(ALPHA_S);

  private String content;
  boolean[] contains = new boolean[128];
  boolean noASCII = false;

  private Constant(String content) {
    Arrays.fill(contains, false);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < content.length(); i++) {
      int c = content.codePointAt(i);
      if (c < 128) {
        contains[c] = true;
      } else {
        sb.appendCodePoint(c);
      }
    }
    if (sb.length() > 0) {
      noASCII = true;
      this.content = sb.toString();
    }
  }

  public boolean has(int c) {
    return (c < 128) ? contains[c] : noASCII && content.indexOf(c) != -1;
  }

  public boolean hasNo(int c) {
    return !has(c);
  }

  public boolean has(int c, String additional) {
    return has(c) || additional.indexOf(c) != -1;
  }

  public boolean hasNo(int c, String additional) {
    return !has(c, additional);
  }
}
