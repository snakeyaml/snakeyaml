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
package org.yaml.snakeyaml.issues.issue132;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.nodes.Node;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=132
 */
public class ScalarEventTagTest extends TestCase {
    public void testLoad() {
        Yaml yaml = new Yaml();
        Iterable<Event> parsed = yaml.parse(new StringReader("5"));
        List<Event> events = new ArrayList<Event>(5);
        for (Event event : parsed) {
            events.add(event);
            // System.out.println(event);
        }
        String tag = ((ScalarEvent) events.get(2)).getTag();
        assertNull("The tag should not be specified: " + tag, tag);
    }

    public void testDump() {
        Yaml yaml = new Yaml();
        Node intNode = yaml.represent(7);
        assertEquals("tag:yaml.org,2002:int", intNode.getTag().toString());
        // System.out.println(intNode);
        List<Event> intEvents = yaml.serialize(intNode);
        String tag = ((ScalarEvent) intEvents.get(2)).getTag();
        assertEquals("Without the tag emitter would not know how to emit '7'",
                "tag:yaml.org,2002:int", tag);
        //
        Node strNode = yaml.represent("7");
        assertEquals("tag:yaml.org,2002:str", strNode.getTag().toString());
        // System.out.println(strNode);
    }
}
