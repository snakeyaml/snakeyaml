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
package org.yaml.snakeyaml.issues.issue137;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

/**
 * http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
 */
public class SupplementaryCharactersTest extends TestCase {

  public static class EmojiContainer {

    public Map<String, Map<String, Integer>> sizes;
    public Map<String, Map<String, List<String>>> values;
  }

  public void testSupplementaryCharacter() {
    Yaml yaml = new Yaml();
    String parsed = yaml.load("\"\\U0001f648\"");
    assertEquals("\ud83d\ude48", parsed);
    // System.out.println(data);
  }

  public void testBasicMultilingualPlane() {
    Yaml yaml = new Yaml();
    String parsed = yaml.load("\"\\U00000041\"");
    assertEquals("A", parsed);
  }

  /**
   * Supplementary code points are dumped normally
   */
  public void testDumpSupplementaryCodePoint() throws UnsupportedEncodingException {
    String supplementary = "\ud83d\ude48";
    Yaml yaml = new Yaml();
    String output = yaml.dump(supplementary);
    assertEquals("\ud83d\ude48\n", output);
    String binString = yaml.load(output);
    assertEquals(supplementary, binString);
  }

  /**
   * Non-printable characters are escaped
   */
  public void testDumpNonPrintableCharacter() throws UnsupportedEncodingException {
    String supplementary = "\u0001";
    Yaml yaml = new Yaml();
    String output = yaml.dump(supplementary);
    assertEquals("!!binary |-\n  AQ==\n", output);
    byte[] binary = yaml.load(output);
    String binString = new String(binary, StandardCharsets.UTF_8);
    assertEquals(supplementary, binString);
  }

  public void testDumpSurrogateCharacter() throws UnsupportedEncodingException {
    String supplementary = "\ud83d";
    Yaml yaml = new Yaml();
    try {
      yaml.dump(supplementary);
      fail("dumping half code point without other half should fail");
    } catch (Exception e) {
      assertEquals("invalid string value has occurred", e.getMessage());
    }

  }

  public void testLoadSupplementaryCodePoint() {
    new Yaml().load("\"\ud83d\ude48\"\n");
  }

  public void testLoadSurrogateCharacter() {
    try {
      new Yaml().load("\"\ud83d\"\n");
      fail("separate surrogate characters are not printable");
    } catch (Exception e) {
      assertEquals("special characters are not allowed", e.getMessage());
    }
  }

  /*
   * This method tests loading of the document with a lot of SupplementaryCharacters. Main purpose
   * is to check that StreamReader actually reads document fully, but not in one read (since file is
   * bigger than StreamReader buffer).
   */
  public void testLoadingEmoji() {
    InputStream input = this.getClass().getClassLoader().getResourceAsStream("issues/emoji.yaml");
    EmojiContainer emoji = new Yaml().loadAs(input, EmojiContainer.class);

    assertEquals(emoji.sizes.keySet(), emoji.values.keySet());

    for (Map.Entry<String, Map<String, Integer>> mainTopic : emoji.sizes.entrySet()) {
      String mainName = mainTopic.getKey();
      Map<String, Integer> subtopic2size = mainTopic.getValue();

      Map<String, List<String>> subtopic2values = emoji.values.get(mainName);
      assertEquals(subtopic2size.keySet(), subtopic2values.keySet());

      for (Map.Entry<String, Integer> subTopic : subtopic2size.entrySet()) {
        String subName = subTopic.getKey();

        assertEquals(subTopic.getValue().intValue(), subtopic2values.get(subName).size());
      }
    }

  }
}
