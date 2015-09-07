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
package org.yaml.snakeyaml.issues.issue173;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public class RecursiveAnchorTest extends TestCase {

    public void testWithoutCustomStyle() throws Exception {
        Map<String, Map<String, Object>> rootMap = new HashMap<String, Map<String, Object>>();
        Map<String, Object> enclosedMap = new HashMap<String, Object>();
        enclosedMap.put("world", "test");
        rootMap.put("test", enclosedMap);
        Yaml yaml = new Yaml();
        String output = yaml.dump(rootMap);
        assertEquals("test: {world: test}\n", output);
    }

    public void testWithBlockStyle() throws Exception {
        Map<String, Map<String, Object>> rootMap = new HashMap<String, Map<String, Object>>();
        Map<String, Object> enclosedMap = new HashMap<String, Object>();
        enclosedMap.put("world", "test");
        rootMap.put("test", enclosedMap);

        DumperOptions yamlOptions = new DumperOptions();
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        Representer yamlRepresenter = new Representer();

        Yaml yaml = new Yaml(yamlRepresenter, yamlOptions);
        String output = yaml.dump(rootMap);
        assertEquals("test:\n  world: test\n", output);
    }
}
