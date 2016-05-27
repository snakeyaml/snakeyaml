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
package org.yaml.snakeyaml;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.events.*;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlParseTest extends TestCase {

    public void testParse() {
        Yaml yaml = new Yaml();
        Event e = null;
        int counter = 0;
        for (Event event : yaml.parse(new StringReader("abc: 56"))) {
            if (e == null) {
                assertTrue(event instanceof StreamStartEvent);
            }
            e = event;
            counter++;
        }
        assertTrue(e instanceof StreamEndEvent);
        assertEquals(8, counter);
    }

    public void testParseEvents() {
        Yaml yaml = new Yaml();
        Iterator<Event> events = yaml.parse(new StringReader("%YAML 1.1\n---\na")).iterator();
        assertTrue(events.next() instanceof StreamStartEvent);
        DocumentStartEvent documentStartEvent = (DocumentStartEvent) events.next();
        assertTrue(documentStartEvent.getExplicit());
        assertEquals(DumperOptions.Version.V1_1, documentStartEvent.getVersion());
        Map<String, String> DEFAULT_TAGS = new HashMap<String, String>();
        DEFAULT_TAGS.put("!", "!");
        DEFAULT_TAGS.put("!!", Tag.PREFIX);
        assertEquals(DEFAULT_TAGS, documentStartEvent.getTags());
        ScalarEvent scalarEvent = (ScalarEvent) events.next();
        assertNull(scalarEvent.getAnchor());
        assertNull(scalarEvent.getTag());
        assertEquals(new ImplicitTuple(true, false).toString(), scalarEvent.getImplicit().toString());
        DocumentEndEvent documentEndEvent = (DocumentEndEvent) events.next();
        assertFalse(documentEndEvent.getExplicit());
        assertTrue("Unexpected event.", events.next() instanceof StreamEndEvent);
        assertFalse(events.hasNext());
    }

    public void testParseManyDocuments() {
        Yaml yaml = new Yaml();
        Event e = null;
        int counter = 0;
        for (Event event : yaml.parse(new StringReader("abc: 56\n---\n4\n---\nqwe\n"))) {
            if (e == null) {
                assertTrue(event instanceof StreamStartEvent);
            }
            e = event;
            counter++;
        }
        assertTrue(e instanceof StreamEndEvent);
        assertEquals(14, counter);
    }
}
