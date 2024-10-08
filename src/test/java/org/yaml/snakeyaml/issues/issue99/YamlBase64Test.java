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
package org.yaml.snakeyaml.issues.issue99;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import junit.framework.TestCase;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlDocument;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * Example for issue 99
 *
 * @see <a href="http://code.google.com/p/snakeyaml/issues/detail?id=99">issue 99</a>
 */
public class YamlBase64Test extends TestCase {

  /**
   * test base64 decoding
   */
  public void testBase64() throws IOException {
    String text = Util.getLocalResource("issues/issue99-base64_literal.yaml");
    String[] lines = text.split("\n");
    String all = "";
    for (int i = 1; i < lines.length; i++) {// skip first line
      all = all + lines[i].trim();
    }
    // System.out.println(all);
    byte[] decoded = Base64.getDecoder().decode(all);
    assertEquals(3737, decoded.length);
    checkBytes(decoded);
  }

  public void testYamlBase64Loading() throws IOException {
    Yaml yaml = new Yaml();
    InputStream inputStream =
        YamlBase64Test.class.getResourceAsStream("/issues/issue99-base64_double_quoted.yaml");
    Map<String, Object> bean = yaml.load(inputStream);
    byte[] jpeg = (byte[]) bean.get("jpegPhoto");
    checkBytes(jpeg);
    inputStream.close();
  }

  private void checkBytes(byte[] jpeg) throws IOException {
    InputStream input;
    input = YamlDocument.class.getClassLoader().getResourceAsStream("issues/issue99.jpeg");
    BufferedInputStream is = new BufferedInputStream(input);
    int i = 0;
    while (i < jpeg.length) {
      int etalon = is.read();
      if (jpeg[i] < 0) {
        assertEquals(etalon, jpeg[i] + 256);
      } else {
        assertEquals(etalon, jpeg[i]);
      }
      i++;
    }
    is.close();
  }

  /**
   * In the literal scalar all the line breaks are significant
   *
   * @throws IOException
   */
  public void testYamlBase64LoadingLiteral() throws IOException {
    Yaml yaml = new Yaml();
    InputStream inputStream =
        YamlBase64Test.class.getResourceAsStream("/issues/issue99-base64_literal.yaml");
    Map<String, Object> bean = yaml.load(inputStream);
    byte[] jpeg = (byte[]) bean.get("jpegPhoto");
    checkBytes(jpeg);
    inputStream.close();
  }

  /**
   * Redefine the !!binary global tag in a way that it ignores all the white spaces to be able to
   * use literal scalar
   */
  @SuppressWarnings("unchecked")
  public void testRedefineBinaryTag() throws IOException {
    Yaml yaml = new Yaml(new SpecialContructor(Tag.BINARY));
    InputStream inputStream =
        YamlBase64Test.class.getResourceAsStream("/issues/issue99-base64_literal.yaml");
    Map<String, Object> bean = yaml.load(inputStream);
    byte[] jpeg = (byte[]) bean.get("jpegPhoto");
    checkBytes(jpeg);
    inputStream.close();
  }

  private class SpecialContructor extends Constructor {

    public SpecialContructor(Tag tag) {
      super(new LoaderOptions());
      this.yamlConstructors.put(tag, new MyBinaryConstructor());
    }

    private class MyBinaryConstructor extends AbstractConstruct {

      public Object construct(Node node) {
        String contentWithNewLines = constructScalar((ScalarNode) node);
        String noNewLines = contentWithNewLines.replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(noNewLines);
        return decoded;
      }
    }
  }

  /**
   * Define a local tag to ignore all the white spaces to be able to use literal scalar
   */
  public void testLocalBinaryTag() throws IOException {
    Yaml yaml = new Yaml(new SpecialContructor(new Tag("!beautiful")));
    InputStream inputStream =
        YamlBase64Test.class.getResourceAsStream("/issues/issue99-base64_literal_custom_tag.yaml");
    Map<String, Object> bean = yaml.load(inputStream);
    byte[] jpeg = (byte[]) bean.get("jpegPhoto");
    checkBytes(jpeg);
    inputStream.close();
  }
}
