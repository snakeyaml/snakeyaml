/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.StringReader;

import junit.framework.TestCase;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;

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
