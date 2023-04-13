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
package org.yaml.snakeyaml.emitter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;

/**
 * Test issue 39 in Engine
 */
public class EmitCommentAndSpacesTest extends TestCase {

  public void testEmitCommentWithEvent() throws IOException {
    String input = "first:\n  second: abc\n  \n  \n\n";

    Parser parser = new ParserImpl(new StreamReader(input), new LoaderOptions());
    StringWriter writer = new StringWriter();
    Emitter emitter = new Emitter(writer, new DumperOptions());
    List<Event> events = new ArrayList<>();
    while (parser.peekEvent() != null) {
      Event event = parser.getEvent();
      events.add(event);
      emitter.emit(event);
    }
    String output = "first:\n  second: abc\n";

    assertEquals(11, events.size());
    assertEquals("abc", ((ScalarEvent) events.get(6)).getValue());
    assertEquals(output, writer.toString());
  }
}
