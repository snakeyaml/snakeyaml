package org.yaml.snakeyaml.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.comments.CommentType;
import org.yaml.snakeyaml.events.CommentEvent;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;

public class ProblematicYamlTest {
    private static final LoaderOptions LOAD_OPTIONS = new LoaderOptions();
    static {
        LOAD_OPTIONS.setProcessComments(true);
    }
    private static final DumperOptions DUMPER_OPTIONS = new DumperOptions();
    static {
        DUMPER_OPTIONS.setDefaultFlowStyle(FlowStyle.BLOCK);
    }

    private void assertEventListEquals(List<ID> expectedEventIdList, List<CommentType> expectedCommentTypeList,
            Parser parser) {
        Iterator<CommentType> commentTypeIterator = expectedCommentTypeList.iterator();
        for (ID expectedEventId : expectedEventIdList) {
            parser.checkEvent(expectedEventId);
            Event event = parser.getEvent();
            System.out.println("Expected: " + expectedEventId);
            if (event == null) {
                fail("Missing event: " + expectedEventId);
            }
            System.out.println("Got: " + event
                    + (event.getEventId() == ID.Comment ? " " + ((CommentEvent) event).getCommentType() : ""));
            System.out.println();
            if (expectedCommentTypeList != null && event.getEventId() == ID.Comment) {
                assertEquals(commentTypeIterator.next(), ((CommentEvent) event).getCommentType());
            }
            assertEquals(expectedEventId, event.getEventId());
        }
    }

    @SuppressWarnings("unused")
    private void printEventList(Parser parser) {
        for (Event event = parser.getEvent(); event != null; event = parser.getEvent()) {
            System.out.println("Got: " + event
                    + (event.getEventId() == ID.Comment ? " " + ((CommentEvent) event).getCommentType() : ""));
            System.out.println();
        }
    }

    @Test
    public void testParseProblematicYaml1() throws Exception {
        final String yamlString1 = "" + //
                "key: value\n" + //
                "  # Comment 1\n" + // s.b BLOCK, classified as INLINE
                "\n" + //
                "  # Comment 2\n" + //
                "";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { //
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, //
                ID.Scalar, //
                ID.Comment, //
                ID.Comment, //
                ID.Comment, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd, //
        });

        List<CommentType> expectedCommentTypeList = Arrays.asList(new CommentType[] { //
                CommentType.BLOCK, CommentType.BLANK_LINE, CommentType.BLOCK, });

        ParserImpl parser = new ParserImpl(new StreamReader(new StringReader(yamlString1)),
                LOAD_OPTIONS.isProcessComments());

        assertEventListEquals(expectedEventIdList, expectedCommentTypeList, parser);
    }

    @Test
    public void testParseProblematicYaml2() throws Exception {
        final String yamlString2 = "" + //
                "key: value\n" + //
                "\n" + //
                "  # Comment 1\n" + // s.b BLOCK, classified as INLINE
                "\n" + //
                "  # Comment 2\n" + //
                "";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { //
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, //
                ID.Scalar, //
                ID.Comment, //
                ID.Comment, //
                ID.Comment, //
                ID.Comment, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd, //
        });

        List<CommentType> expectedCommentTypeList = Arrays.asList(new CommentType[] { //
                CommentType.BLANK_LINE, CommentType.BLOCK, CommentType.BLANK_LINE, CommentType.BLOCK });

        ParserImpl parser = new ParserImpl(new StreamReader(new StringReader(yamlString2)),
                LOAD_OPTIONS.isProcessComments());

        assertEventListEquals(expectedEventIdList, expectedCommentTypeList, parser);
    }

    @Test
    public void testParseProblematicYaml3() throws Exception {
        final String yamlString3 = "" + //
                "key: value\n" + //
                "\n" + //
                "key: value\n" + //
                "";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { //
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, //
                ID.Scalar, //
                ID.Comment, //
                ID.Scalar, //
                ID.Scalar, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd, //
        });

        List<CommentType> expectedCommentTypeList = Arrays.asList(new CommentType[] { //
                CommentType.BLANK_LINE });

        ParserImpl parser = new ParserImpl(new StreamReader(new StringReader(yamlString3)),
                LOAD_OPTIONS.isProcessComments());

        assertEventListEquals(expectedEventIdList, expectedCommentTypeList, parser);
    }

    @Test
    public void test() throws Exception {
        String s = "" + //
                "---\n" + //
                "in the block context:\n" + //
                "    indentation should be kept: { \n" + //
                "    but in the flow context: [\n" + //
                "it may be violated]\n" + //
                "}\n" + //
                "---\n" + //
                "the parser does not require scalars\n" + //
                "to be indented with at least one space\n" + //
                "...\n" + //
                "---\n" + //
                "\"the parser does not require scalars\n" + //
                "to be indented with at least one space\"\n" + //
                "---\n" + //
                "foo:\n" + //
                "    bar: 'quoted scalars\n" + //
                "may not adhere indentation'\n" + //
                "";
        Parser parser = new ParserImpl(new StreamReader(new StringReader(s)));
        List<Event> events = new ArrayList<Event>();
        while (parser.peekEvent() != null) {
            Event e = parser.getEvent();
            System.out.println(e);
            events.add(e);
        }
    }
   
}
