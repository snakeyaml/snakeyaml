/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.parser;

import java.util.ArrayList;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.reader.Reader;

public class ParserImplTest extends TestCase {

    public void testGetEvent() {
        String data = "string: abcd";
        Reader reader = new Reader(data);
        Parser parser = new ParserImpl(reader);
        Mark dummyMark = new Mark("dummy", 0, 0, 0, "", 0);
        LinkedList<Event> etalonEvents = new LinkedList<Event>();
        etalonEvents.add(new StreamStartEvent(dummyMark, dummyMark));
        etalonEvents.add(new DocumentStartEvent(dummyMark, dummyMark, false, null, null));
        etalonEvents.add(new MappingStartEvent(null, null, true, dummyMark, dummyMark,
                Boolean.FALSE));
        boolean[] implicit = { true, false };
        etalonEvents.add(new ScalarEvent(null, null, implicit, "string", dummyMark, dummyMark,
                (char) 0));
        etalonEvents.add(new ScalarEvent(null, null, implicit, "abcd", dummyMark, dummyMark,
                (char) 0));
        etalonEvents.add(new MappingEndEvent(dummyMark, dummyMark));
        etalonEvents.add(new DocumentEndEvent(dummyMark, dummyMark, false));
        etalonEvents.add(new StreamEndEvent(dummyMark, dummyMark));
        while (parser.checkEvent(new ArrayList<Class<? extends Event>>())) {
            Event event = parser.getEvent();
            if (etalonEvents.isEmpty()) {
                fail("unexpected event: " + event);
            }
            assertEquals(etalonEvents.removeFirst(), event);
            // System.out.println(event);
        }
        assertFalse("Must contain no more events: " + parser.getEvent(), parser
                .checkEvent(new ArrayList<Class<? extends Event>>()));
    }

    public void testGetEvent2() {
        String data = "american:\n  - Boston Red Sox";
        Reader reader = new Reader(data);
        Parser parser = new ParserImpl(reader);
        Mark dummyMark = new Mark("dummy", 0, 0, 0, "", 0);
        LinkedList<Event> etalonEvents = new LinkedList<Event>();
        etalonEvents.add(new StreamStartEvent(dummyMark, dummyMark));
        etalonEvents.add(new DocumentStartEvent(dummyMark, dummyMark, false, null, null));
        etalonEvents
                .add(new MappingStartEvent(null, null, true, dummyMark, dummyMark, Boolean.TRUE));
        boolean[] implicit = { true, false };
        etalonEvents.add(new ScalarEvent(null, null, implicit, "american", dummyMark, dummyMark,
                (char) 0));
        etalonEvents.add(new SequenceStartEvent(null, null, true, dummyMark, dummyMark,
                Boolean.FALSE));
        etalonEvents.add(new ScalarEvent(null, null, implicit, "Boston Red Sox", dummyMark,
                dummyMark, (char) 0));
        etalonEvents.add(new SequenceEndEvent(dummyMark, dummyMark));
        etalonEvents.add(new MappingEndEvent(dummyMark, dummyMark));
        etalonEvents.add(new DocumentEndEvent(dummyMark, dummyMark, false));
        etalonEvents.add(new StreamEndEvent(dummyMark, dummyMark));
        while (parser.checkEvent(new ArrayList<Class<? extends Event>>())) {
            Event event = parser.getEvent();
            if (etalonEvents.isEmpty()) {
                fail("unexpected event: " + event);
            }
            assertEquals(etalonEvents.removeFirst(), event);
            // System.out.println(event);
        }
        assertFalse("Must contain no more events: " + parser.getEvent(), parser
                .checkEvent(new ArrayList<Class<? extends Event>>()));
    }
}
