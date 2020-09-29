package org.yaml.snakeyaml.comment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.Event.ID;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.tokens.Token;

public class ParserWithCommentEnabledTest {

    private void assertEventListEquals(List<ID> expectedEventIdList, Parser parser) {
        for (ID expectedEventId : expectedEventIdList) {
            parser.checkEvent(expectedEventId);
            Event event = parser.getEvent();
            System.out.println(event);
            if (event == null) {
                fail("Missing event: " + expectedEventId);
            }
            assertEquals(expectedEventId, event.getEventId());
        }
    }

    @SuppressWarnings("unused")
    private void printEventList(Parser parser) {
        for (Event event = parser.getEvent(); event != null; event = parser.getEvent()) {
            System.out.println(event);
        }
    }

    @Test
    public void testEmpty() {
        List<ID> expectedEventIdList = Arrays.asList(new ID[] { ID.StreamStart, ID.StreamEnd });

        String data = "";

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void testParseWithOnlyComment() {
        String data = "# Comment";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { //
                ID.StreamStart, //
                ID.Comment, //
                ID.StreamEnd, //
        });

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void testCommentEndingALine() {
        String data = "" + //
                "key: # Comment\n" + //
                "  value\n";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Comment, ID.Scalar, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd });

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void testMultiLineComment() {
        String data = "" + //
                "key: # Comment\n" + //
                "     # lines\n" + //
                "  value\n" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Comment, ID.Comment, ID.Scalar, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd });

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void testBlankLine() {
        String data = "" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { ID.StreamStart, //
                ID.Comment, //
                ID.StreamEnd });

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void testBlankLineComments() {
        String data = "" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { ID.StreamStart, //
                ID.Comment, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Scalar, ID.Comment, //
                ID.Comment, //
                ID.Comment, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd });

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void test_blockScalar() {
        String data = "" + //
                "abc: > # Comment\n" + //
                "    def\n" + //
                "    hij\n" + //
                "\n";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { //
                ID.StreamStart, //
                ID.DocumentStart, //
                ID.MappingStart, //
                ID.Scalar, ID.Comment, //
                ID.Scalar, //
                ID.MappingEnd, //
                ID.DocumentEnd, //
                ID.StreamEnd //
        });

        Parser sut = new ParserImpl(new StreamReader(data), true);

        assertEventListEquals(expectedEventIdList, sut);
    }

    @Test
    public void testDirectiveLineEndComment() {
        String data = "%YAML 1.1 #Comment\n";

        List<ID> expectedEventIdList = Arrays.asList(new ID[] { //
                ID.StreamStart, //
                ID.StreamEnd //
        });

        Parser sut = new ParserImpl(new StreamReader(data), true);
        assertEventListEquals(expectedEventIdList, sut);
    }
}
