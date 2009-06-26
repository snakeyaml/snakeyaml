/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.emitter.EventsLoader;
import org.yaml.snakeyaml.events.CollectionStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.NodeEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 * @see imported from PyYAML
 */
public class PyEmitterTest extends PyImportTest {
    public void testEmitterOnData() throws IOException {
        _testEmitter(".data", false);
    }

    public void testEmitterOnCanonicalNormally() throws IOException {
        _testEmitter(".canonical", false);
    }

    public void testEmitterOnCanonicalCanonically() throws IOException {
        _testEmitter(".canonical", true);
    }

    private void _testEmitter(String mask, boolean canonical) throws IOException {
        File[] files = getStreamsByExtension(mask, true);
        assertTrue("No test files found.", files.length > 0);
        for (File file : files) {
            // if (!file.getName().contains("spec-06-01.canonical")) {
            // continue;
            // }
            try {
                List<Event> events = parse(new FileInputStream(file));
                //
                StringWriter stream = new StringWriter();
                DumperOptions options = new DumperOptions();
                options.setCanonical(canonical);
                Emitter emitter = new Emitter(stream, options);
                for (Event event : events) {
                    emitter.emit(event);
                }
                //
                String data = stream.toString();
                List<Event> newEvents = new LinkedList<Event>();
                Reader reader = new Reader(data);
                Parser parser = new ParserImpl(reader);
                while (parser.peekEvent() != null) {
                    Event event = parser.getEvent();
                    newEvents.add(event);
                }
                // check
                assertEquals(events.size(), newEvents.size());
                Iterator<Event> iter1 = events.iterator();
                Iterator<Event> iter2 = newEvents.iterator();
                while (iter1.hasNext()) {
                    Event event = iter1.next();
                    Event newEvent = iter2.next();
                    assertEquals(event.getClass().getName(), newEvent.getClass().getName());
                    if (event instanceof NodeEvent) {
                        NodeEvent e1 = (NodeEvent) event;
                        NodeEvent e2 = (NodeEvent) newEvent;
                        assertEquals(e1.getAnchor(), e2.getAnchor());
                    }
                    if (event instanceof CollectionStartEvent) {
                        CollectionStartEvent e1 = (CollectionStartEvent) event;
                        CollectionStartEvent e2 = (CollectionStartEvent) newEvent;
                        assertEquals(e1.getTag(), e2.getTag());
                    }
                    if (event instanceof ScalarEvent) {
                        ScalarEvent e1 = (ScalarEvent) event;
                        ScalarEvent e2 = (ScalarEvent) newEvent;
                        boolean[] implicit1 = e1.getImplicit();
                        boolean[] implicit2 = e2.getImplicit();
                        if (!implicit1[0] && !implicit1[1] && !implicit2[0] && !implicit2[1]) {
                            assertEquals(e1.getTag(), e2.getTag());
                        }
                        assertEquals(e1.getValue(), e2.getValue());
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testEmitterStyles() throws IOException {
        File[] canonicalFiles = getStreamsByExtension(".canonical", false);
        assertTrue("No test files found.", canonicalFiles.length > 0);
        File[] dataFiles = getStreamsByExtension(".data", true);
        assertTrue("No test files found.", dataFiles.length > 0);
        List<File> allFiles = new LinkedList(Arrays.asList(canonicalFiles));
        allFiles.addAll(Arrays.asList(dataFiles));
        for (File file : allFiles) {
            try {
                List<Event> events = new LinkedList<Event>();
                Reader reader = new Reader(new UnicodeReader(new FileInputStream(file)));
                Parser parser = new ParserImpl(reader);
                while (parser.peekEvent() != null) {
                    Event event = parser.getEvent();
                    events.add(event);
                }
                //
                for (Boolean flowStyle : new Boolean[] { Boolean.FALSE, Boolean.TRUE }) {
                    for (DumperOptions.ScalarStyle style : DumperOptions.ScalarStyle.values()) {
                        List<Event> styledEvents = new LinkedList<Event>();
                        for (Event event : events) {
                            if (event instanceof ScalarEvent) {
                                ScalarEvent scalar = (ScalarEvent) event;
                                event = new ScalarEvent(scalar.getAnchor(), scalar.getTag(), scalar
                                        .getImplicit(), scalar.getValue(), scalar.getStartMark(),
                                        scalar.getEndMark(), style.getChar());
                            } else if (event instanceof SequenceStartEvent) {
                                SequenceStartEvent seqStart = (SequenceStartEvent) event;
                                event = new SequenceStartEvent(seqStart.getAnchor(), seqStart
                                        .getTag(), seqStart.getImplicit(), seqStart.getStartMark(),
                                        seqStart.getEndMark(), flowStyle);
                            } else if (event instanceof MappingStartEvent) {
                                MappingStartEvent mapStart = (MappingStartEvent) event;
                                event = new MappingStartEvent(mapStart.getAnchor(), mapStart
                                        .getTag(), mapStart.getImplicit(), mapStart.getStartMark(),
                                        mapStart.getEndMark(), flowStyle);
                            }
                            styledEvents.add(event);
                        }
                        // emit
                        String data = emit(styledEvents);
                        List<Event> newEvents = parse(data);
                        assertEquals("Events must not change. File: " + file, events.size(),
                                newEvents.size());
                        Iterator<Event> oldIter = events.iterator();
                        Iterator<Event> newIter = newEvents.iterator();
                        while (oldIter.hasNext()) {
                            Event event = oldIter.next();
                            Event newEvent = newIter.next();
                            assertEquals(event.getClass(), newEvent.getClass());
                            if (event instanceof NodeEvent) {
                                assertEquals(((NodeEvent) event).getAnchor(),
                                        ((NodeEvent) newEvent).getAnchor());
                            }
                            if (event instanceof CollectionStartEvent) {
                                assertEquals(((CollectionStartEvent) event).getTag(),
                                        ((CollectionStartEvent) newEvent).getTag());
                            }
                            if (event instanceof ScalarEvent) {
                                ScalarEvent scalarOld = (ScalarEvent) event;
                                ScalarEvent scalarNew = (ScalarEvent) newEvent;
                                boolean[] oldImplicit = scalarOld.getImplicit();
                                boolean[] newImplicit = scalarNew.getImplicit();
                                if (!oldImplicit[0] && !oldImplicit[1] && !newImplicit[0]
                                        && !newImplicit[1]) {
                                    assertEquals(scalarOld.getTag(), scalarNew.getTag());
                                }
                                assertEquals(scalarOld.getValue(), scalarNew.getValue());
                            }
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private String emit(List<Event> events) throws IOException {
        StringWriter writer = new StringWriter();
        Emitter emitter = new Emitter(writer, new DumperOptions());
        for (Event event : events) {
            emitter.emit(event);
        }
        return writer.toString();
    }

    private List<Event> parse(String data) {
        ParserImpl parser = new ParserImpl(new Reader(data));
        List<Event> newEvents = new LinkedList<Event>();
        while (parser.peekEvent() != null) {
            newEvents.add(parser.getEvent());
        }
        return newEvents;
    }

    @SuppressWarnings("unchecked")
    public void testEmitterEvents() throws IOException {
        File[] files = getStreamsByExtension(".events", false);
        assertTrue("No test files found.", files.length > 0);
        for (File file : files) {
            // if (!file.getName().contains("spec-06-01.canonical")) {
            // continue;
            // }
            try {
                Loader loader = new EventsLoader();
                List<Event> events = new LinkedList<Event>();
                String content = getResource(file.getName());
                events = (List<Event>) load(loader, content);
                //
                StringWriter stream = new StringWriter();
                Emitter emitter = new Emitter(stream, new DumperOptions());
                for (Event event : events) {
                    emitter.emit(event);
                }
                //
                String data = stream.toString();
                List<Event> newEvents = new LinkedList<Event>();
                Reader reader = new Reader(data);
                Parser parser = new ParserImpl(reader);
                while (parser.peekEvent() != null) {
                    Event event = parser.getEvent();
                    newEvents.add(event);
                }
                // check
                assertEquals(events.size(), newEvents.size());
                Iterator<Event> iter1 = events.iterator();
                Iterator<Event> iter2 = newEvents.iterator();
                while (iter1.hasNext()) {
                    Event event = iter1.next();
                    Event newEvent = iter2.next();
                    assertEquals(event.getClass().getName(), newEvent.getClass().getName());
                    if (event instanceof NodeEvent) {
                        NodeEvent e1 = (NodeEvent) event;
                        NodeEvent e2 = (NodeEvent) newEvent;
                        assertEquals(e1.getAnchor(), e2.getAnchor());
                    }
                    if (event instanceof CollectionStartEvent) {
                        CollectionStartEvent e1 = (CollectionStartEvent) event;
                        CollectionStartEvent e2 = (CollectionStartEvent) newEvent;
                        assertEquals(e1.getTag(), e2.getTag());
                    }
                    if (event instanceof ScalarEvent) {
                        ScalarEvent e1 = (ScalarEvent) event;
                        ScalarEvent e2 = (ScalarEvent) newEvent;
                        boolean[] implicit1 = e1.getImplicit();
                        boolean[] implicit2 = e2.getImplicit();
                        if (implicit1[0] == implicit2[0] && implicit1[1] == implicit2[1]) {

                        } else {
                            if ((e1.getTag() == null || e2.getTag() == null)
                                    || e1.getTag().equals(e2.getTag())) {
                            } else {
                                System.out.println("tag1: " + e1.getTag());
                                System.out.println("tag2: " + e2.getTag());
                                fail("in file: " + file);
                            }
                        }
                        assertEquals(e1.getValue(), e2.getValue());
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed File: " + file);
                // fail("Failed File: " + file + "; " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
