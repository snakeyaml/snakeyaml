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
package examples;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class AnyObjectExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testLoad() {
        String doc = Util.getLocalResource("examples/any-object-example.yaml");
        Yaml yaml = new Yaml();
        Map<String, Object> object = (Map<String, Object>) yaml.load(doc);
        assertEquals(6, object.size());
        assertEquals("[null, null]", object.get("none").toString());
        List<?> list1 = (List<?>) object.get("none");
        assertEquals(2, list1.size());
        for (Object object2 : list1) {
            assertNull(object2);
        }
        //
        assertEquals("[true, false, true, false]", object.get("bool").toString());
        assertEquals(4, ((List<?>) object.get("bool")).size());
        //
        assertEquals(new Integer(42), object.get("int"));
        assertEquals(new Double(3.14159), object.get("float"));
        //
        assertEquals("[LITE, RES_ACID, SUS_DEXT]", object.get("list").toString());
        List<?> list2 = (List<?>) object.get("list");
        assertEquals(3, list2.size());
        for (Object object2 : list2) {
            assertEquals(object2.toString(), object2.toString().toUpperCase());
        }
        //
        assertEquals("{hp=13, sp=5}", object.get("dict").toString());
        Map<String, Integer> map = (Map<String, Integer>) object.get("dict");
        assertEquals(2, map.keySet().size());
        assertEquals(new Integer(13), map.get("hp"));
        assertEquals(new Integer(5), map.get("sp"));
    }
}
