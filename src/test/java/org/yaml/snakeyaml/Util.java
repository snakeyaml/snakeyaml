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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Util {

  public static InputStream getInputstream(String theName) {
    try {
      InputStream input;
      input = YamlDocument.class.getClassLoader().getResourceAsStream(theName);
      if (input == null) {
        throw new RuntimeException("Can not find " + theName);
      }
      return new BufferedInputStream(input);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getLocalResource(String theName) {
    try (InputStream input = YamlDocument.class.getClassLoader().getResourceAsStream(theName)) {
      if (input == null) {
        throw new RuntimeException("Cannot find " + theName);
      }

      InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
      BufferedReader bufferedReader = new BufferedReader(reader);

      StringBuilder buffer = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        buffer.append(line).append("\n");
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
