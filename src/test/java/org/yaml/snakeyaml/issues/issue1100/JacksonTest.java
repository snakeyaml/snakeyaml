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
package org.yaml.snakeyaml.issues.issue1100;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;

import static org.junit.Assert.assertTrue;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1100
 */
public class JacksonTest {

  public static String yamlData() {
    return "configs:\n" + "- aa: \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"\n" + "  aaaaaa:\n"
        + "    aaaa: \"-----------------------------------\"\n" + "    aaaaaaaaaa:\n"
        + "      aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:\n"
        + "        aaaaaaaaa: \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"\n"
        + "        aaaaaaaaaa: \"aaaaaaaaaaaaaaaaaaaaaaaa\"\n" + "        aaaaaaaa: \"aa\"\n"
        + "        aaaa: \"aaaaaaaaa\"\n" + "      aaaaaaaaa:\n" + "        aaaa: \"aaaaa\"\n"
        + "        aaaaa:\n" + "          aaaaa: \"aaaaaaaaaaaaaaaaaaaaaaaaaa\"\n"
        + "          aaaaaa: \"aaaaaaaaaaaaaaaaaaaaaaaaaa\"\n"
        + "          aaaaaa: \"aaaaaaaaaaaaaaaaaaaaa\"\n"
        + "    aaaaaaaaa: \"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"\n" + "    aaaa: aaaaa\n"
        + "  aaaa:\n" + "    aaa: \"aaaaaaaaa\"\n"
        + "- bb: \"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\"\n" + "  bbbbbbb:\n"
        + "    bbbb: \"bbbb\"\n" + "    bbbbbbbb: \"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\"\n"
        + "    bbbb: bbbbb\n" + "    bbbbbbbbbb:\n" + "      bbbbbbbbbb:\n"
        + "        bbbb: \"bbbbb\"\n" + "        bbbbb:\n"
        + "          bbbbb: \"bbbbbbbbbbbbbbbbbbbbbbbbbb\"\n" + "  bbbb:\n"
        + "    bbb: \"bbbbbbbbb\"\n" + "- cc: \"cccccccccccccccccccccccccccccccccccc\"\n"
        + "  cccccc:\n"
        + "    issue: \"cccc cc cc ccc \uD83E\uDD84\uD83E\uDD96\uD83D\uDC10\uD83E\uDD84\uD83E\uDD96\uD83D\uDC10\uD83E\uDD84\uD83E\uDD96\uD83D\uDC10\"\n"
        + "    cccccccc: \"ccccccccccccccccccccccccccccccccccccccccc\"\n" + "    cccc: cccc\n"
        + "    cccccccccc:\n" + "      ccccccccc:\n" + "        cccc: \"ccccc\"\n"
        + "        ccccc:\n" + "          ccccc: \"ccccccccccccccccccccccc\"\n" + "  cccc:\n"
        + "    ccc: \"ccccccccc\"\n";
  }

  public ObjectMapper createYamlMapper() {
    return new YAMLMapper(YAMLFactory.builder().loaderOptions(new LoaderOptions()).build())
        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  @Test
  public void loadAsYamlMapper() throws JsonProcessingException {
    ObjectMapper yamlObjectMapper = createYamlMapper();
    YamlRoot yamlRoot = yamlObjectMapper.readValue(yamlData(), YamlRoot.class);
    String output = yamlObjectMapper.writeValueAsString(yamlRoot);
    assertTrue(output.contains(
        "\uD83E\uDD84\uD83E\uDD96\uD83D\uDC10\uD83E\uDD84\uD83E\uDD96\uD83D\uDC10\uD83E\uDD84\uD83E\uDD96\uD83D\uDC10"));
  }
}
