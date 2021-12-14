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
package org.yaml.snakeyaml.issues.issue386;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class DuplicateKeysOrderTest {

    @Test
    public void deleteDuplicatKeysInCorrectOrder() {
        String input = Util.getLocalResource("issues/issue386-duplicate-keys-order.yaml");
        Yaml yaml = new Yaml();
        Map<String, String> testMap = yaml.load(input);

        assertThat("Number of keys in map", testMap.size(), is(4));
        assertThat(testMap.keySet(), hasItems("key1", "key2", "key3", "lostone"));

        assertThat(testMap.get("key1"), is("1st-2"));
        assertThat(testMap.get("key2"), is("2nd-2"));
        assertThat(testMap.get("key3"), is("3rd-3"));
        assertThat(testMap.get("lostone"), is("Not meeee!!!"));
    }

}
