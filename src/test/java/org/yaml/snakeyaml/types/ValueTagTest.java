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
package org.yaml.snakeyaml.types;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.YamlDocument;

/**
 * @see http://yaml.org/type/value.html
 */
public class ValueTagTest extends AbstractTest {

    /**
     * The 'value' tag does not work as defined in the specification but exactly
     * as in PyYAML
     */
    @SuppressWarnings("unchecked")
    public void testValue() {
        InputStream input = YamlDocument.class.getClassLoader().getResourceAsStream(
                YamlDocument.ROOT + "types/value.yaml");
        Yaml yaml = new Yaml();
        Iterator<Object> iter = (Iterator<Object>) yaml.loadAll(input).iterator();
        Map<String, List<String>> oldSchema = (Map<String, List<String>>) iter.next();
        assertEquals(1, oldSchema.size());
        List<String> list = oldSchema.get("link with");
        assertEquals(2, list.size());
        assertEquals("library1.dll", list.get(0));
        assertEquals("library2.dll", list.get(1));
        //
        Map<String, List<Map<String, String>>> newSchema = (Map<String, List<Map<String, String>>>) iter
                .next();
        assertEquals(1, newSchema.size());
        //
        List<Map<String, String>> list2 = newSchema.get("link with");
        assertEquals(2, list2.size());
        Map<String, String> map1 = list2.get(0);
        assertEquals(2, map1.size());
        assertEquals("library1.dll", map1.get("="));
        assertEquals(new Double(1.2), map1.get("version"));
        assertFalse(iter.hasNext());
    }
}
