/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.issues.issue436;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class NonScalarLineBreakTest extends TestCase {

    public void testValidMap() {
        Yaml yaml = new Yaml();
        String validMap =
                "{\r\n \"response\": \"a\" \r\n}";
        Map<String, String> ggg = yaml.load(validMap);
        assertEquals("a", ggg.get("response"));
        String output = yaml.dump(ggg);
        assertEquals("{response: a}\n", output);

    }

    public void testInvalidMap() {
        Yaml yaml = new Yaml();
        String invalidMap = "--- |-\n" +
                "  {\n" +
                "    \"response\" : \"\"\n" +
                "  }";
        String noMap = yaml.load(invalidMap);
        assertEquals("{\n" +
                "  \"response\" : \"\"\n" +
                "}", noMap);
    }

    public void testJackson() throws JsonProcessingException {
        ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory().enable(YAMLGenerator.Feature.MINIMIZE_QUOTES));
        String what = yamlObjectMapper.writeValueAsString("{\\n  \"response\" : \"\"\\n}");
        assertEquals("--- '{\\n  \"response\" : \"\"\\n}'\n", what);
    }
}
