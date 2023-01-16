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
package examples;

import java.io.StringReader;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlCreator;

public class CustomJavaObjectWithBinaryStringTest extends TestCase {

  public static class Pojo {

    private String data;

    public Pojo() {}

    public Pojo(String data) {
      this.data = data;
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((data == null) ? 0 : data.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Pojo other = (Pojo) obj;
      if (data == null) {
        return other.data == null;
      } else {
        return data.equals(other.data);
      }
    }

  }

  public void testDump() {
    Yaml yaml = YamlCreator.allowClassPrefix("examples");
    Pojo expected = new Pojo(new String(new byte[] {13, 14, 15, 16}));
    String output = yaml.dump(expected);

    assertTrue(output.contains("data: !!binary |-"));
    assertTrue(output.contains("DQ4PEA=="));

    Pojo actual = yaml.load(new StringReader(output));
    assertEquals(expected, actual);
  }
}
