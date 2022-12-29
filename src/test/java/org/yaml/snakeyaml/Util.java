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
package org.yaml.snakeyaml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Util {

  public static String getLocalResource(String theName) {
    try {
      InputStream input;
      input = YamlDocument.class.getClassLoader().getResourceAsStream(theName);
      if (input == null) {
        throw new RuntimeException("Can not find " + theName);
      }
      BufferedInputStream is = new BufferedInputStream(input);
      StringBuilder buf = new StringBuilder(3000);
      int i;
      try {
        while ((i = is.read()) != -1) {
          buf.append((char) i);
        }
      } finally {
        is.close();
      }
      String resource = buf.toString();
      // convert EOLs
      String[] lines = resource.split("\\r?\\n");
      StringBuilder buffer = new StringBuilder();
      for (int j = 0; j < lines.length; j++) {
        buffer.append(lines[j]);
        buffer.append("\n");
      }
      return buffer.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean compareAllLines(String text1, String text2) {
    Set<String> split1 = new HashSet<String>(Arrays.asList(text1.trim().split("\n")));
    Set<String> split2 = new HashSet<String>(Arrays.asList(text2.trim().split("\n")));
    return split1.containsAll(split2) && split2.containsAll(split1);
  }
}
