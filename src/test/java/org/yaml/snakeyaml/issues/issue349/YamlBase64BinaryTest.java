/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue349;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class YamlBase64BinaryTest extends TestCase {

    public void testLocalBinaryTags() throws IOException {
        String[] names = {"1", "2", "3"};
        for (String name : names) {
            toBeTested(name);
        }
    }

    public void toBeTested(String name) throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlBase64BinaryTest.class
                .getResourceAsStream("/issues/issue349-" + name + ".yaml");
        Map<String, Object> bean = (Map<String, Object>) yaml.load(inputStream);
        byte[] jpeg = (byte[]) bean.get("picture");
        assertEquals(65, jpeg.length);
        inputStream.close();
    }
}
