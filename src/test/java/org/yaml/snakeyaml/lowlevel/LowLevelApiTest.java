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
package org.yaml.snakeyaml.lowlevel;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.Node;

public class LowLevelApiTest extends TestCase {

    public void testLowLevel() {
        List<Object> list = new ArrayList<Object>();
        list.add(1);
        list.add("abc");
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "Tolstoy");
        map.put("book", "War and People");
        list.add(map);
        Yaml yaml = new Yaml();
        String etalon = yaml.dump(list);
        // System.out.println(etalon);
        //
        Node node = yaml.represent(list);
        // System.out.println(node);
        assertEquals(
                "Representation tree from an object and from its YAML document must be the same.",
                yaml.compose(new StringReader(etalon)).toString(), node.toString());
        //
        List<Event> events = yaml.serialize(node);
        int i = 0;
        for (Event etalonEvent : yaml.parse(new StringReader(etalon))) {
            Event ev1 = events.get(i++);
            assertEquals(etalonEvent.getClass(), ev1.getClass());
            if (etalonEvent instanceof ScalarEvent) {
                ScalarEvent scalar1 = (ScalarEvent) etalonEvent;
                ScalarEvent scalar2 = (ScalarEvent) ev1;
                assertEquals(scalar1.getAnchor(), scalar2.getAnchor());
                assertEquals(scalar1.getValue(), scalar2.getValue());
            }
        }
        assertEquals(i, events.size());
    }
}
