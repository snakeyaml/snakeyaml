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
package examples.resolver;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

public class CustomResolverTest extends TestCase {

    public void testResolverToDump() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("1.0", "2009-01-01");
        Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(),
                new CustomResolver());
        String output = yaml.dump(map);
        assertEquals("{1.0: 2009-01-01}\n", output);
        assertEquals("Float and Date must be escaped.", "{'1.0': '2009-01-01'}\n",
                new Yaml().dump(map));
    }

    @SuppressWarnings("unchecked")
    public void testResolverToLoad() {
        Yaml yaml = new Yaml(new Constructor(), new Representer(), new DumperOptions(),
                new CustomResolver());
        Map<Object, Object> map = (Map<Object, Object>) yaml.load("1.0: 2009-01-01");
        assertEquals(1, map.size());
        assertEquals("2009-01-01", map.get("1.0"));
        // the default Resolver shall create Date and Double from the same YAML
        // document
        Yaml yaml2 = new Yaml();
        Map<Object, Object> map2 = (Map<Object, Object>) yaml2.load("1.0: 2009-01-01");
        assertEquals(1, map2.size());
        assertFalse(map2.containsKey("1.0"));
        assertTrue(map2.toString(), map2.containsKey(new Double(1.0)));
    }
}
