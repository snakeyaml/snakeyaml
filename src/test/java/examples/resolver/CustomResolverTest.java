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

package examples.resolver;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.SnakeYaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class CustomResolverTest extends TestCase {

    public void testResolverToDump() {
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put("1.0", "2009-01-01");
        SnakeYaml yaml = new SnakeYaml(new Constructor(), new Dumper(new DumperOptions()),
                new CustomResolver());
        String output = yaml.dump(map);
        assertEquals("{1.0: 2009-01-01}\n", output);
        assertEquals("Float and Date must be escaped.", "{'1.0': '2009-01-01'}\n", new SnakeYaml()
                .dump(map));
    }

    @SuppressWarnings("unchecked")
    public void testResolverToLoad() {
        SnakeYaml yaml = new SnakeYaml(new Constructor(), new Dumper(new DumperOptions()),
                new CustomResolver());
        Map<Object, Object> map = (Map<Object, Object>) yaml.load("1.0: 2009-01-01");
        assertEquals(1, map.size());
        assertEquals("2009-01-01", map.get("1.0"));
        //
        SnakeYaml yaml2 = new SnakeYaml();
        Map<Object, Object> map2 = (Map<Object, Object>) yaml2.load("1.0: 2009-01-01");
        assertEquals(1, map2.size());
        assertFalse(map2.containsKey("1.0"));
        assertTrue(map2.toString(), map2.containsKey(new Double(1.0)));
    }
}
