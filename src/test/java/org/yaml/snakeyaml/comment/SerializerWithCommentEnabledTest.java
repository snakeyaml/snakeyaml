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
package org.yaml.snakeyaml.comment;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.resolver.Resolver;
import org.yaml.snakeyaml.serializer.Serializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SerializerWithCommentEnabledTest {
    private boolean DEBUG = false;

    private void println(String s) {
        if (DEBUG) System.out.println(s);
    }

    private void println() {
        if (DEBUG) System.out.println();
    }

    private void assertEventListEquals(List<ID> expectedEventIdList, List<Event> actualEvents) {
        Iterator<Event> iterator = actualEvents.iterator();
        for (ID expectedEventId : expectedEventIdList) {
            println("Expected: " + expectedEventId);
            assertTrue(iterator.hasNext());
            Event event = iterator.next();
            println("Got: " + event);
            println();
            assertEquals(expectedEventId, event.getEventId());
        }
    }

    private static class TestEmitter implements Emitable {
        private List<Event> eventList = new ArrayList<>();

        @Override
        public void emit(Event event) throws IOException {
            eventList.add(event);
        }

        public List<Event> getEventList() {
            return eventList;
        }
    }

    public List<Event> serializeWithCommentsEnabled(String data) throws IOException {
        TestEmitter emitter = new TestEmitter();
        Tag rootTag = null;
        Serializer serializer = new Serializer(emitter, new Resolver(), new DumperOptions(), rootTag);
        serializer.open();
        Composer composer = new Composer(new ParserImpl(new StreamReader(data), true), new Resolver());
        while (composer.checkNode()) {
            serializer.serialize(composer.getNode());
        }
        serializer.close();
        List<Event> events = emitter.getEventList();
        println("RESULT: ");
        for (Event event : events) {
            println(event.toString());
        }
        println();
        return events;
    }


    @Test
    public void testEmpty() throws Exception {
        List<ID> expectedEventIdList = Arrays.asList(ID.StreamStart, ID.StreamEnd);

        String data = "";

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testParseWithOnlyComment() throws Exception {
        String data = "# Comment";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testCommentEndingALine() throws Exception {
        String data = "" + //
                "key: # Comment\n" + //
                "  value\n";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Comment, ID.Scalar, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd);

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testMultiLineComment() throws Exception {
        String data = "" + //
                "key: # Comment\n" + //
                "     # lines\n" + //
                "  value\n" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Comment, ID.Comment, ID.Scalar, //
                ID.MappingEnd, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd);

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testBlankLine() throws Exception {
        String data = "" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd);

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testBlankLineComments() throws Exception {
        String data = "" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Comment, //
                ID.Scalar, ID.Scalar, ID.Comment, //
                ID.MappingEnd, //
                ID.Comment, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd);

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void test_blockScalar() throws Exception {
        String data = "" + //
                "abc: > # Comment\n" + //
                "    def\n" + //
                "    hij\n" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Comment, //
                ID.Scalar, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testDirectiveLineEndComment() throws Exception {
        String data = "%YAML 1.1 #Comment\n";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testSequence() throws Exception {
        String data = "" + //
                "# Comment\n" + //
                "list: # InlineComment1\n" + //
                "# Block Comment\n" + //
                "- item # InlineComment2\n" + //
                "# Comment\n";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Comment, //
                ID.Scalar, ID.Comment, //
                ID.SequenceStart, //
                ID.Comment, //
                ID.Scalar, ID.Comment, //
                ID.SequenceEnd, //
                ID.MappingEnd, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testAllComments1() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "key: # Inline Comment1a\n" + //
                "     # Inline Comment1b\n" + //
                "  # Block Comment3a\n" + //
                "  # Block Comment3b\n" + //
                "  value # Inline Comment2\n" + //
                "# Block Comment4\n" + //
                "list: # InlineComment3a\n" + //
                "      # InlineComment3b\n" + //
                "# Block Comment5\n" + //
                "- item1 # InlineComment4\n" + //
                "- item2: [ value2a, value2b ] # InlineComment5\n" + //
                "- item3: { key3a: [ value3a1, value3a2 ], key3b: value3b } # InlineComment6\n" + //
                "# Block Comment6\n" + //
                "---\n" + //
                "# Block Comment7\n" + //
                "";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Comment, //
                ID.Comment, //
                ID.Scalar, ID.Comment, ID.Comment, //

                ID.Comment, ID.Comment, //
                ID.Scalar, ID.Comment, //

                ID.Comment, //
                ID.Scalar, ID.Comment, ID.Comment, //

                ID.SequenceStart, //
                ID.Comment, //
                ID.Scalar, //
                ID.Comment, //

                ID.MappingStart, //
                ID.Scalar, ID.SequenceStart, ID.Scalar, ID.Scalar, ID.SequenceEnd, ID.Comment, //
                ID.MappingEnd,

                ID.MappingStart, //
                ID.Scalar, // value=item3
                ID.MappingStart, //
                ID.Scalar, // value=key3a
                ID.SequenceStart, //
                ID.Scalar, // value=value3a
                ID.Scalar, // value=value3a2
                ID.SequenceEnd, //
                ID.Scalar, // value=key3b
                ID.Scalar, // value=value3b
                ID.MappingEnd, //
                ID.Comment, // type=IN_LINE, value= InlineComment6
                ID.MappingEnd, //
                ID.SequenceEnd, //
                ID.MappingEnd, //
                ID.Comment, //
                ID.DocumentEnd, //

                ID.DocumentStart, //
                ID.Comment, //
                ID.Scalar, // Empty
                ID.DocumentEnd, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testAllComments2() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "- item1 # Inline Comment1a\n" + //
                "        # Inline Comment1b\n" + //
                "# Block Comment3a\n" + //
                "# Block Comment3b\n" + //
                "- item2: value # Inline Comment2\n" + //
                "# Block Comment4\n" + //
                "";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.SequenceStart, //
                ID.Comment, //
                ID.Comment, //
                ID.Scalar, ID.Comment, ID.Comment, //
                ID.MappingStart, //
                ID.Comment, //
                ID.Comment, //
                ID.Scalar, ID.Scalar, ID.Comment, //
                ID.MappingEnd, //
                ID.SequenceEnd, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }

    @Test
    public void testAllComments3() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "[ item1, item2: value2, {item3: value3} ] # Inline Comment1\n" + //
                "# Block Comment2\n" + //
                "";

        List<ID> expectedEventIdList = Arrays.asList(//
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.Comment, //
                ID.SequenceStart, //
                ID.Scalar, ID.MappingStart, //
                ID.Scalar, ID.Scalar, //
                ID.MappingEnd, //
                ID.MappingStart, //
                ID.Scalar, ID.Scalar, //
                ID.MappingEnd, //
                ID.SequenceEnd, //
                ID.Comment, //
                ID.Comment, //
                ID.DocumentEnd, //
                ID.StreamEnd //
        );

        List<Event> result = serializeWithCommentsEnabled(data);

        assertEventListEquals(expectedEventIdList, result);
    }
}
