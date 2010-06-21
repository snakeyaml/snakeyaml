/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

package org.yaml.snakeyaml.issues.issue68;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class NonAsciiCharacterTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testLoad() {
        Yaml yaml = new Yaml();
        Map<String, Map<String, String>> obj = (Map<String, Map<String, String>>) yaml
                .load("test.string: {en: И}");
        assertEquals(1, obj.size());
        assertEquals("Map: " + obj.toString(), "И", obj.get("test.string").get("en"));
    }
}
