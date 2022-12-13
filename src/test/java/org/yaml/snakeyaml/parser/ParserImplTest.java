/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.parser;

import java.util.LinkedList;
import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.events.DocumentEndEvent;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;
import org.yaml.snakeyaml.reader.StreamReader;

public class ParserImplTest extends TestCase {

  private void check(LinkedList<Event> etalonEvents, Parser parser) {
    for (Event etalonEvent : etalonEvents) {
      parser.checkEvent(etalonEvent.getEventId());
      Event event = parser.getEvent();
      if (event == null) {
        fail("Missing event: " + etalonEvent);
      }
      assertEquals(etalonEvent, event);
    }
  }

  public void testGetEventWithTag() {
    String data = "! 12";
    StreamReader reader = new StreamReader(data);
    Parser parser = new ParserImpl(reader, new LoaderOptions());
    Mark dummyMark = new Mark("dummy", 0, 0, 0, "".toCharArray(), 0);
    LinkedList<Event> etalonEvents = new LinkedList<Event>();
    etalonEvents.add(new StreamStartEvent(dummyMark, dummyMark));
    etalonEvents.add(new DocumentStartEvent(dummyMark, dummyMark, false, null, null));
    etalonEvents.add(new ScalarEvent(null, "!", new ImplicitTuple(true, false), "12", dummyMark,
        dummyMark, DumperOptions.ScalarStyle.PLAIN));
    etalonEvents.add(new DocumentEndEvent(dummyMark, dummyMark, false));
    etalonEvents.add(new StreamEndEvent(dummyMark, dummyMark));
    check(etalonEvents, parser);
  }

  public void testGetEvent() {
    String data = "string: abcd";
    StreamReader reader = new StreamReader(data);
    Parser parser = new ParserImpl(reader, new LoaderOptions());
    Mark dummyMark = new Mark("dummy", 0, 0, 0, "".toCharArray(), 0);
    LinkedList<Event> etalonEvents = new LinkedList<Event>();
    etalonEvents.add(new StreamStartEvent(dummyMark, dummyMark));
    etalonEvents.add(new DocumentStartEvent(dummyMark, dummyMark, false, null, null));
    etalonEvents.add(new MappingStartEvent(null, null, true, dummyMark, dummyMark,
        DumperOptions.FlowStyle.BLOCK));
    etalonEvents.add(new ScalarEvent(null, null, new ImplicitTuple(true, false), "string",
        dummyMark, dummyMark, DumperOptions.ScalarStyle.PLAIN));
    etalonEvents.add(new ScalarEvent(null, null, new ImplicitTuple(true, false), "abcd", dummyMark,
        dummyMark, DumperOptions.ScalarStyle.PLAIN));
    etalonEvents.add(new MappingEndEvent(dummyMark, dummyMark));
    etalonEvents.add(new DocumentEndEvent(dummyMark, dummyMark, false));
    etalonEvents.add(new StreamEndEvent(dummyMark, dummyMark));
    check(etalonEvents, parser);
  }

  public void testGetEvent2() {
    String data = "american:\n  - Boston Red Sox";
    StreamReader reader = new StreamReader(data);
    Parser parser = new ParserImpl(reader, new LoaderOptions());
    Mark dummyMark = new Mark("dummy", 0, 0, 0, "".toCharArray(), 0);
    LinkedList<Event> etalonEvents = new LinkedList<Event>();
    etalonEvents.add(new StreamStartEvent(dummyMark, dummyMark));
    etalonEvents.add(new DocumentStartEvent(dummyMark, dummyMark, false, null, null));
    etalonEvents.add(new MappingStartEvent(null, null, true, dummyMark, dummyMark,
        DumperOptions.FlowStyle.FLOW));
    etalonEvents.add(new ScalarEvent(null, null, new ImplicitTuple(true, false), "american",
        dummyMark, dummyMark, DumperOptions.ScalarStyle.PLAIN));
    etalonEvents.add(new SequenceStartEvent(null, null, true, dummyMark, dummyMark,
        DumperOptions.FlowStyle.BLOCK));
    etalonEvents.add(new ScalarEvent(null, null, new ImplicitTuple(true, false), "Boston Red Sox",
        dummyMark, dummyMark, DumperOptions.ScalarStyle.PLAIN));
    etalonEvents.add(new SequenceEndEvent(dummyMark, dummyMark));
    etalonEvents.add(new MappingEndEvent(dummyMark, dummyMark));
    etalonEvents.add(new DocumentEndEvent(dummyMark, dummyMark, false));
    etalonEvents.add(new StreamEndEvent(dummyMark, dummyMark));
    check(etalonEvents, parser);
  }
}
