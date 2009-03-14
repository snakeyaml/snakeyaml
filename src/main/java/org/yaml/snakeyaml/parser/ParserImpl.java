/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.AliasEvent;
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
import org.yaml.snakeyaml.scanner.Scanner;
import org.yaml.snakeyaml.scanner.ScannerImpl;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.DocumentEndToken;
import org.yaml.snakeyaml.tokens.DocumentStartToken;
import org.yaml.snakeyaml.tokens.FlowEntryToken;
import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.ValueToken;

/**
 * <pre>
 * # The following YAML grammar is LL(1) and is parsed by a recursive descent
 * parser.
 * stream            ::= STREAM-START implicit_document? explicit_document* STREAM-END
 * implicit_document ::= block_node DOCUMENT-END*
 * explicit_document ::= DIRECTIVE* DOCUMENT-START block_node? DOCUMENT-END*
 * block_node_or_indentless_sequence ::=
 *                       ALIAS
 *                       | properties (block_content | indentless_block_sequence)?
 *                       | block_content
 *                       | indentless_block_sequence
 * block_node        ::= ALIAS
 *                       | properties block_content?
 *                       | block_content
 * flow_node         ::= ALIAS
 *                       | properties flow_content?
 *                       | flow_content
 * properties        ::= TAG ANCHOR? | ANCHOR TAG?
 * block_content     ::= block_collection | flow_collection | SCALAR
 * flow_content      ::= flow_collection | SCALAR
 * block_collection  ::= block_sequence | block_mapping
 * flow_collection   ::= flow_sequence | flow_mapping
 * block_sequence    ::= BLOCK-SEQUENCE-START (BLOCK-ENTRY block_node?)* BLOCK-END
 * indentless_sequence   ::= (BLOCK-ENTRY block_node?)+
 * block_mapping     ::= BLOCK-MAPPING_START
 *                       ((KEY block_node_or_indentless_sequence?)?
 *                       (VALUE block_node_or_indentless_sequence?)?)*
 *                       BLOCK-END
 * flow_sequence     ::= FLOW-SEQUENCE-START
 *                       (flow_sequence_entry FLOW-ENTRY)*
 *                       flow_sequence_entry?
 *                       FLOW-SEQUENCE-END
 * flow_sequence_entry   ::= flow_node | KEY flow_node? (VALUE flow_node?)?
 * flow_mapping      ::= FLOW-MAPPING-START
 *                       (flow_mapping_entry FLOW-ENTRY)*
 *                       flow_mapping_entry?
 *                       FLOW-MAPPING-END
 * flow_mapping_entry    ::= flow_node | KEY flow_node? (VALUE flow_node?)?
 * FIRST sets:
 * stream: { STREAM-START }
 * explicit_document: { DIRECTIVE DOCUMENT-START }
 * implicit_document: FIRST(block_node)
 * block_node: { ALIAS TAG ANCHOR SCALAR BLOCK-SEQUENCE-START BLOCK-MAPPING-START FLOW-SEQUENCE-START FLOW-MAPPING-START }
 * flow_node: { ALIAS ANCHOR TAG SCALAR FLOW-SEQUENCE-START FLOW-MAPPING-START }
 * block_content: { BLOCK-SEQUENCE-START BLOCK-MAPPING-START FLOW-SEQUENCE-START FLOW-MAPPING-START SCALAR }
 * flow_content: { FLOW-SEQUENCE-START FLOW-MAPPING-START SCALAR }
 * block_collection: { BLOCK-SEQUENCE-START BLOCK-MAPPING-START }
 * flow_collection: { FLOW-SEQUENCE-START FLOW-MAPPING-START }
 * block_sequence: { BLOCK-SEQUENCE-START }
 * block_mapping: { BLOCK-MAPPING-START }
 * block_node_or_indentless_sequence: { ALIAS ANCHOR TAG SCALAR BLOCK-SEQUENCE-START BLOCK-MAPPING-START FLOW-SEQUENCE-START FLOW-MAPPING-START BLOCK-ENTRY }
 * indentless_sequence: { ENTRY }
 * flow_collection: { FLOW-SEQUENCE-START FLOW-MAPPING-START }
 * flow_sequence: { FLOW-SEQUENCE-START }
 * flow_mapping: { FLOW-MAPPING-START }
 * flow_sequence_entry: { ALIAS ANCHOR TAG SCALAR FLOW-SEQUENCE-START FLOW-MAPPING-START KEY }
 * flow_mapping_entry: { ALIAS ANCHOR TAG SCALAR FLOW-SEQUENCE-START FLOW-MAPPING-START KEY }
 * </pre>
 * 
 * Since writing a recursive-descendant parser is a straightforward task, we do
 * not give many comments here.
 * 
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class ParserImpl implements Parser {
    private static final Map<String, String> DEFAULT_TAGS = new HashMap<String, String>();
    static {
        DEFAULT_TAGS.put("!", "!");
        DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
    }

    private final Scanner scanner;
    private Event currentEvent;
    private List<Integer> yamlVersion;
    private Map<String, String> tagHandles;
    private final LinkedList<Production> states;
    private final LinkedList<Mark> marks;
    private Production state;

    public ParserImpl(org.yaml.snakeyaml.reader.Reader reader) {
        this.scanner = new ScannerImpl(reader);
        currentEvent = null;
        yamlVersion = null;
        tagHandles = new HashMap<String, String>();
        states = new LinkedList<Production>();
        marks = new LinkedList<Mark>();
        state = new ParseStreamStart();
    }

    /**
     * Check the type of the next event.
     */
    public boolean checkEvent(List<Class<? extends Event>> choices) {
        peekEvent();
        if (currentEvent != null) {
            if (choices.size() == 0) {
                return true;
            }
            for (Class<? extends Event> class1 : choices) {
                if (class1.isInstance(currentEvent)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check the type of the next event.
     */
    public boolean checkEvent(Class<? extends Event> cls) {
        List<Class<? extends Event>> list = new ArrayList<Class<? extends Event>>(1);
        list.add(cls);
        return checkEvent(list);
    }

    /*
     * Get the next event.
     */
    public Event peekEvent() {
        if (currentEvent == null) {
            if (state != null) {
                currentEvent = state.produce();
            }
        }
        return currentEvent;
    }

    /*
     * Get the next event and proceed further.
     */
    public Event getEvent() {
        peekEvent();
        Event value = currentEvent;
        currentEvent = null;
        return value;
    }

    /**
     * <pre>
     * stream    ::= STREAM-START implicit_document? explicit_document* STREAM-END
     * implicit_document ::= block_node DOCUMENT-END*
     * explicit_document ::= DIRECTIVE* DOCUMENT-START block_node? DOCUMENT-END*
     * </pre>
     */
    private class ParseStreamStart implements Production {
        public Event produce() {
            // Parse the stream start.
            StreamStartToken token = (StreamStartToken) scanner.getToken();
            Event event = new StreamStartEvent(token.getStartMark(), token.getEndMark());
            // Prepare the next state.
            state = new ParseImplicitDocumentStart();
            return event;
        }
    }

    private class ParseImplicitDocumentStart implements Production {
        public Event produce() {
            // Parse an implicit document.
            List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
            choices.add(DirectiveToken.class);
            choices.add(DocumentStartToken.class);
            choices.add(StreamEndToken.class);
            if (!scanner.checkToken(choices)) {
                tagHandles = DEFAULT_TAGS;
                Token token = scanner.peekToken();
                Mark startMark = token.getStartMark();
                Mark endMark = startMark;
                Event event = new DocumentStartEvent(startMark, endMark, false, null, null);
                // Prepare the next state.
                states.add(new ParseDocumentEnd());
                state = new ParseBlockNode();
                return event;
            } else {
                Production p = new ParseDocumentStart();
                return p.produce();
            }
        }
    }

    private class ParseDocumentStart implements Production {
        @SuppressWarnings("unchecked")
        public Event produce() {
            // Parse any extra document end indicators.
            while (scanner.checkToken(DocumentEndToken.class)) {
                scanner.getToken();
            }
            // Parse an explicit document.
            Event event;
            if (!scanner.checkToken(StreamEndToken.class)) {
                Token token = scanner.peekToken();
                Mark startMark = token.getStartMark();
                List<Object> version_tags = processDirectives();
                List<Object> version = (List<Object>) version_tags.get(0);
                Map<String, String> tags = (Map<String, String>) version_tags.get(1);
                if (!scanner.checkToken(DocumentStartToken.class)) {
                    throw new ParserException(null, null, "expected '<document start>', but found "
                            + scanner.peekToken().getTokenId(), scanner.peekToken().getStartMark());
                }
                token = scanner.getToken();
                Mark endMark = token.getEndMark();
                Integer[] versionInteger;
                if (version != null) {
                    versionInteger = new Integer[2];
                    versionInteger = version.toArray(versionInteger);
                } else {
                    versionInteger = null;
                }
                event = new DocumentStartEvent(startMark, endMark, true, versionInteger, tags);
                states.add(new ParseDocumentEnd());
                state = new ParseDocumentContent();
            } else {
                // Parse the end of the stream.
                StreamEndToken token = (StreamEndToken) scanner.getToken();
                event = new StreamEndEvent(token.getStartMark(), token.getEndMark());
                if (!states.isEmpty()) {
                    throw new YAMLException("Unexpected end of stream. States left: " + states);
                }
                if (!marks.isEmpty()) {
                    throw new YAMLException("Unexpected end of stream. Marks left: " + marks);
                }
                state = null;
            }
            return event;
        }
    }

    private class ParseDocumentEnd implements Production {
        public Event produce() {
            // Parse the document end.
            Token token = scanner.peekToken();
            Mark startMark = token.getStartMark();
            Mark endMark = startMark;
            boolean explicit = false;
            if (scanner.checkToken(DocumentEndToken.class)) {
                token = scanner.getToken();
                endMark = token.getEndMark();
                explicit = true;
            }
            Event event = new DocumentEndEvent(startMark, endMark, explicit);
            // Prepare the next state.
            state = new ParseDocumentStart();
            return event;
        }
    }

    private class ParseDocumentContent implements Production {
        public Event produce() {
            List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
            choices.add(DirectiveToken.class);
            choices.add(DocumentStartToken.class);
            choices.add(DocumentEndToken.class);
            choices.add(StreamEndToken.class);
            Event event;
            if (scanner.checkToken(choices)) {
                event = processEmptyScalar(scanner.peekToken().getStartMark());
                state = states.removeLast();
                return event;
            } else {
                Production p = new ParseBlockNode();
                return p.produce();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private List<Object> processDirectives() {
        yamlVersion = null;
        tagHandles = new HashMap<String, String>();
        while (scanner.checkToken(DirectiveToken.class)) {
            DirectiveToken token = (DirectiveToken) scanner.getToken();
            if (token.getName().equals("YAML")) {
                if (yamlVersion != null) {
                    throw new ParserException(null, null, "found duplicate YAML directive", token
                            .getStartMark());
                }
                List<Integer> value = (List<Integer>) token.getValue();
                Integer major = value.get(0);
                if (major != 1) {
                    throw new ParserException(null, null,
                            "found incompatible YAML document (version 1.* is required)", token
                                    .getStartMark());
                }
                yamlVersion = (List<Integer>) token.getValue();
            } else if (token.getName().equals("TAG")) {
                List<String> value = (List<String>) token.getValue();
                String handle = value.get(0);
                String prefix = value.get(1);
                if (tagHandles.containsKey(handle)) {
                    throw new ParserException(null, null, "duplicate tag handle " + handle, token
                            .getStartMark());
                }
                tagHandles.put(handle, prefix);
            }
        }
        List<Object> value = new ArrayList<Object>(2);
        value.add(yamlVersion);
        if (!tagHandles.isEmpty()) {
            value.add(new HashMap<String, String>(tagHandles));
        } else {
            value.add(new HashMap<String, String>());
        }
        for (String key : DEFAULT_TAGS.keySet()) {
            if (!tagHandles.containsKey(key)) {
                tagHandles.put(key, DEFAULT_TAGS.get(key));
            }
        }
        return value;
    }

    /**
     * <pre>
     *  block_node_or_indentless_sequence ::= ALIAS
     *                | properties (block_content | indentless_block_sequence)?
     *                | block_content
     *                | indentless_block_sequence
     *  block_node    ::= ALIAS
     *                    | properties block_content?
     *                    | block_content
     *  flow_node     ::= ALIAS
     *                    | properties flow_content?
     *                    | flow_content
     *  properties    ::= TAG ANCHOR? | ANCHOR TAG?
     *  block_content     ::= block_collection | flow_collection | SCALAR
     *  flow_content      ::= flow_collection | SCALAR
     *  block_collection  ::= block_sequence | block_mapping
     *  flow_collection   ::= flow_sequence | flow_mapping
     * </pre>
     */

    private class ParseBlockNode implements Production {
        public Event produce() {
            return parseNode(true, false);
        }
    }

    private Event parseFlowNode() {
        return parseNode(false, false);
    }

    private Event parseBlockNodeOrIndentlessSequence() {
        return parseNode(true, true);
    }

    private Event parseNode(boolean block, boolean indentlessSequence) {
        Event event;
        Mark startMark = null;
        Mark endMark = null;
        Mark tagMark = null;
        if (scanner.checkToken(AliasToken.class)) {
            AliasToken token = (AliasToken) scanner.getToken();
            event = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
            state = states.removeLast();
        } else {
            String anchor = null;
            String[] tagTokenTag = null;
            if (scanner.checkToken(AnchorToken.class)) {
                AnchorToken token = (AnchorToken) scanner.getToken();
                startMark = token.getStartMark();
                endMark = token.getEndMark();
                anchor = token.getValue();
                if (scanner.checkToken(TagToken.class)) {
                    TagToken tagToken = (TagToken) scanner.getToken();
                    tagMark = tagToken.getStartMark();
                    endMark = tagToken.getEndMark();
                    tagTokenTag = tagToken.getValue();
                }
            } else if (scanner.checkToken(TagToken.class)) {
                TagToken tagToken = (TagToken) scanner.getToken();
                startMark = tagToken.getStartMark();
                tagMark = startMark;
                endMark = tagToken.getEndMark();
                tagTokenTag = tagToken.getValue();
                if (scanner.checkToken(AnchorToken.class)) {
                    AnchorToken token = (AnchorToken) scanner.getToken();
                    endMark = token.getEndMark();
                    anchor = token.getValue();
                }
            }
            String tag = null;
            if (tagTokenTag != null) {
                String handle = tagTokenTag[0];
                String suffix = tagTokenTag[1];
                if (handle != null) {
                    if (!tagHandles.containsKey(handle)) {
                        throw new ParserException("while parsing a node", startMark,
                                "found undefined tag handle " + handle, tagMark);
                    }
                    tag = tagHandles.get(handle) + suffix;
                } else {
                    tag = suffix;
                }
            }
            if (startMark == null) {
                startMark = scanner.peekToken().getStartMark();
                endMark = startMark;
            }
            event = null;
            boolean implicit = (tag == null || tag.equals("!"));
            if (indentlessSequence && scanner.checkToken(BlockEntryToken.class)) {
                endMark = scanner.peekToken().getEndMark();
                event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark,
                        Boolean.FALSE);
                state = new ParseIndentlessSequenceEntry();
            } else {
                if (scanner.checkToken(ScalarToken.class)) {
                    ScalarToken token = (ScalarToken) scanner.getToken();
                    endMark = token.getEndMark();
                    boolean[] implicitValues = new boolean[2];
                    if ((token.getPlain() && tag == null) || "!".equals(tag)) {
                        implicitValues[0] = true;
                        implicitValues[1] = false;
                    } else if (tag == null) {
                        implicitValues[0] = false;
                        implicitValues[1] = true;
                    } else {
                        implicitValues[0] = false;
                        implicitValues[1] = false;
                    }
                    event = new ScalarEvent(anchor, tag, implicitValues, token.getValue(),
                            startMark, endMark, token.getStyle());
                    state = states.removeLast();
                } else if (scanner.checkToken(FlowSequenceStartToken.class)) {
                    endMark = scanner.peekToken().getEndMark();
                    event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark,
                            Boolean.TRUE);
                    state = new ParseFlowSequenceFirstEntry();
                } else if (scanner.checkToken(FlowMappingStartToken.class)) {
                    endMark = scanner.peekToken().getEndMark();
                    event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark,
                            Boolean.TRUE);
                    state = new ParseFlowMappingFirstKey();
                } else if (block && scanner.checkToken(BlockSequenceStartToken.class)) {
                    endMark = scanner.peekToken().getStartMark();
                    event = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark,
                            Boolean.FALSE);
                    state = new ParseBlockSequenceFirstEntry();
                } else if (block && scanner.checkToken(BlockMappingStartToken.class)) {
                    endMark = scanner.peekToken().getStartMark();
                    event = new MappingStartEvent(anchor, tag, implicit, startMark, endMark,
                            Boolean.FALSE);
                    state = new ParseBlockMappingFirstKey();
                } else if (anchor != null || tag != null) {
                    // Empty scalars are allowed even if a tag or an anchor is
                    // specified.
                    boolean[] implicitValues = new boolean[2];
                    implicitValues[0] = implicit;
                    implicitValues[1] = false;
                    event = new ScalarEvent(anchor, tag, implicitValues, "", startMark, endMark,
                            (char) 0);
                    state = states.removeLast();
                } else {
                    String node;
                    if (block) {
                        node = "block";
                    } else {
                        node = "flow";
                    }
                    Token token = scanner.peekToken();
                    throw new ParserException("while parsing a " + node + " node", startMark,
                            "expected the node content, but found " + token.getTokenId(), token
                                    .getStartMark());
                }
            }
        }
        return event;
    }

    // block_sequence ::= BLOCK-SEQUENCE-START (BLOCK-ENTRY block_node?)*
    // BLOCK-END

    private class ParseBlockSequenceFirstEntry implements Production {
        public Event produce() {
            Token token = scanner.getToken();
            marks.add(token.getStartMark());
            return new ParseBlockSequenceEntry().produce();
        }
    }

    private class ParseBlockSequenceEntry implements Production {
        public Event produce() {
            if (scanner.checkToken(BlockEntryToken.class)) {
                BlockEntryToken token = (BlockEntryToken) scanner.getToken();
                List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                choices.add(BlockEntryToken.class);
                choices.add(BlockEndToken.class);
                if (!scanner.checkToken(choices)) {
                    states.add(new ParseBlockSequenceEntry());
                    return new ParseBlockNode().produce();
                } else {
                    state = new ParseBlockSequenceEntry();
                    return processEmptyScalar(token.getEndMark());
                }
            }
            if (!scanner.checkToken(BlockEndToken.class)) {
                Token token = scanner.peekToken();
                throw new ParserException("while parsing a block collection", marks.getLast(),
                        "expected <block end>, but found " + token.getTokenId(), token
                                .getStartMark());
            }
            Token token = scanner.getToken();
            Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
            state = states.removeLast();
            marks.removeLast();
            return event;
        }
    }

    // indentless_sequence ::= (BLOCK-ENTRY block_node?)+

    private class ParseIndentlessSequenceEntry implements Production {
        public Event produce() {
            if (scanner.checkToken(BlockEntryToken.class)) {
                Token token = scanner.getToken();
                List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                choices.add(BlockEntryToken.class);
                choices.add(KeyToken.class);
                choices.add(ValueToken.class);
                choices.add(BlockEndToken.class);
                if (!scanner.checkToken(choices)) {
                    states.add(new ParseIndentlessSequenceEntry());
                    return new ParseBlockNode().produce();
                } else {
                    state = new ParseIndentlessSequenceEntry();
                    return processEmptyScalar(token.getEndMark());
                }
            }
            Token token = scanner.peekToken();
            Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
            state = states.removeLast();
            return event;
        }
    }

    private class ParseBlockMappingFirstKey implements Production {
        public Event produce() {
            Token token = scanner.getToken();
            marks.add(token.getStartMark());
            return new ParseBlockMappingKey().produce();
        }
    }

    private class ParseBlockMappingKey implements Production {
        public Event produce() {
            if (scanner.checkToken(KeyToken.class)) {
                Token token = scanner.getToken();
                List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                choices.add(KeyToken.class);
                choices.add(ValueToken.class);
                choices.add(BlockEndToken.class);
                if (!scanner.checkToken(choices)) {
                    states.add(new ParseBlockMappingValue());
                    return parseBlockNodeOrIndentlessSequence();
                } else {
                    state = new ParseBlockMappingValue();
                    return processEmptyScalar(token.getEndMark());
                }
            }
            if (!scanner.checkToken(BlockEndToken.class)) {
                Token token = scanner.peekToken();
                throw new ParserException("while parsing a block mapping", marks.getLast(),
                        "expected <block end>, but found " + token.getTokenId(), token
                                .getStartMark());
            }
            Token token = scanner.getToken();
            Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
            state = states.removeLast();
            marks.removeLast();
            return event;
        }
    }

    private class ParseBlockMappingValue implements Production {
        public Event produce() {
            if (scanner.checkToken(ValueToken.class)) {
                Token token = scanner.getToken();
                List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                choices.add(KeyToken.class);
                choices.add(ValueToken.class);
                choices.add(BlockEndToken.class);
                if (!scanner.checkToken(choices)) {
                    states.add(new ParseBlockMappingKey());
                    return parseBlockNodeOrIndentlessSequence();
                } else {
                    state = new ParseBlockMappingKey();
                    return processEmptyScalar(token.getEndMark());
                }
            }
            state = new ParseBlockMappingKey();
            Token token = scanner.peekToken();
            return processEmptyScalar(token.getStartMark());
        }
    }

    /**
     * <pre>
     * flow_sequence     ::= FLOW-SEQUENCE-START
     *                       (flow_sequence_entry FLOW-ENTRY)*
     *                       flow_sequence_entry?
     *                       FLOW-SEQUENCE-END
     * flow_sequence_entry   ::= flow_node | KEY flow_node? (VALUE flow_node?)?
     * Note that while production rules for both flow_sequence_entry and
     * flow_mapping_entry are equal, their interpretations are different.
     * For `flow_sequence_entry`, the part `KEY flow_node? (VALUE flow_node?)?`
     * generate an inline mapping (set syntax).
     * </pre>
     */
    private class ParseFlowSequenceFirstEntry implements Production {
        public Event produce() {
            Token token = scanner.getToken();
            marks.add(token.getStartMark());
            return new ParseFlowSequenceEntry(true).produce();
        }
    }

    private class ParseFlowSequenceEntry implements Production {
        private boolean first = false;

        public ParseFlowSequenceEntry(boolean first) {
            this.first = first;
        }

        public Event produce() {
            if (!scanner.checkToken(FlowSequenceEndToken.class)) {
                if (!first) {
                    if (scanner.checkToken(FlowEntryToken.class)) {
                        scanner.getToken();
                    } else {
                        Token token = scanner.peekToken();
                        throw new ParserException("while parsing a flow sequence", marks.getLast(),
                                "expected ',' or ']', but got " + token.getTokenId(), token
                                        .getStartMark());
                    }
                }
                if (scanner.checkToken(KeyToken.class)) {
                    Token token = scanner.peekToken();
                    Event event = new MappingStartEvent(null, null, true, token.getStartMark(),
                            token.getEndMark(), Boolean.TRUE);
                    state = new ParseFlowSequenceEntryMappingKey();
                    return event;
                } else if (!scanner.checkToken(FlowSequenceEndToken.class)) {
                    states.add(new ParseFlowSequenceEntry(false));
                    return parseFlowNode();
                }
            }
            Token token = scanner.getToken();
            Event event = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
            state = states.removeLast();
            marks.removeLast();
            return event;
        }
    }

    private class ParseFlowSequenceEntryMappingKey implements Production {
        public Event produce() {
            Token token = scanner.getToken();
            List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
            choices.add(ValueToken.class);
            choices.add(FlowEntryToken.class);
            choices.add(FlowSequenceEndToken.class);
            if (!scanner.checkToken(choices)) {
                states.add(new ParseFlowSequenceEntryMappingValue());
                return parseFlowNode();
            } else {
                state = new ParseFlowSequenceEntryMappingValue();
                return processEmptyScalar(token.getEndMark());
            }
        }
    }

    private class ParseFlowSequenceEntryMappingValue implements Production {
        public Event produce() {
            if (scanner.checkToken(ValueToken.class)) {
                Token token = scanner.getToken();
                List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                choices.add(FlowEntryToken.class);
                choices.add(FlowSequenceEndToken.class);
                if (!scanner.checkToken(choices)) {
                    states.add(new ParseFlowSequenceEntryMappingEnd());
                    return parseFlowNode();
                } else {
                    state = new ParseFlowSequenceEntryMappingEnd();
                    return processEmptyScalar(token.getEndMark());
                }
            } else {
                state = new ParseFlowSequenceEntryMappingEnd();
                Token token = scanner.peekToken();
                return processEmptyScalar(token.getStartMark());
            }
        }
    }

    private class ParseFlowSequenceEntryMappingEnd implements Production {
        public Event produce() {
            state = new ParseFlowSequenceEntry(false);
            Token token = scanner.peekToken();
            return new MappingEndEvent(token.getStartMark(), token.getEndMark());
        }
    }

    /**
     * <pre>
     *   flow_mapping  ::= FLOW-MAPPING-START
     *          (flow_mapping_entry FLOW-ENTRY)*
     *          flow_mapping_entry?
     *          FLOW-MAPPING-END
     *   flow_mapping_entry    ::= flow_node | KEY flow_node? (VALUE flow_node?)?
     * </pre>
     */
    private class ParseFlowMappingFirstKey implements Production {
        public Event produce() {
            Token token = scanner.getToken();
            marks.add(token.getStartMark());
            return new ParseFlowMappingKey(true).produce();
        }
    }

    private class ParseFlowMappingKey implements Production {
        private boolean first = false;

        public ParseFlowMappingKey(boolean first) {
            this.first = first;
        }

        public Event produce() {
            if (!scanner.checkToken(FlowMappingEndToken.class)) {
                if (!first) {
                    if (scanner.checkToken(FlowEntryToken.class)) {
                        scanner.getToken();
                    } else {
                        Token token = scanner.peekToken();
                        throw new ParserException("while parsing a flow mapping", marks.getLast(),
                                "expected ',' or '}', but got " + token.getTokenId(), token
                                        .getStartMark());
                    }
                }
                if (scanner.checkToken(KeyToken.class)) {
                    Token token = scanner.getToken();
                    List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                    choices.add(ValueToken.class);
                    choices.add(FlowEntryToken.class);
                    choices.add(FlowMappingEndToken.class);
                    if (!scanner.checkToken(choices)) {
                        states.add(new ParseFlowMappingValue());
                        return parseFlowNode();
                    } else {
                        state = new ParseFlowMappingValue();
                        return processEmptyScalar(token.getEndMark());
                    }
                } else if (!scanner.checkToken(FlowMappingEndToken.class)) {
                    states.add(new ParseFlowMappingEmptyValue());
                    return parseFlowNode();
                }
            }
            Token token = scanner.getToken();
            Event event = new MappingEndEvent(token.getStartMark(), token.getEndMark());
            state = states.removeLast();
            marks.removeLast();
            return event;
        }
    }

    private class ParseFlowMappingValue implements Production {
        public Event produce() {
            if (scanner.checkToken(ValueToken.class)) {
                Token token = scanner.getToken();
                List<Class<? extends Token>> choices = new ArrayList<Class<? extends Token>>();
                choices.add(FlowEntryToken.class);
                choices.add(FlowMappingEndToken.class);
                if (!scanner.checkToken(choices)) {
                    states.add(new ParseFlowMappingKey(false));
                    return parseFlowNode();
                } else {
                    state = new ParseFlowMappingKey(false);
                    return processEmptyScalar(token.getEndMark());
                }
            } else {
                state = new ParseFlowMappingKey(false);
                Token token = scanner.peekToken();
                return processEmptyScalar(token.getStartMark());
            }
        }
    }

    private class ParseFlowMappingEmptyValue implements Production {
        public Event produce() {
            state = new ParseFlowMappingKey(false);
            return processEmptyScalar(scanner.peekToken().getStartMark());
        }
    }

    /**
     * <pre>
     * block_mapping     ::= BLOCK-MAPPING_START
     *           ((KEY block_node_or_indentless_sequence?)?
     *           (VALUE block_node_or_indentless_sequence?)?)*
     *           BLOCK-END
     * </pre>
     */
    private Event processEmptyScalar(Mark mark) {
        boolean[] value = new boolean[2];
        value[0] = true;
        value[1] = false;
        return new ScalarEvent(null, null, value, "", mark, mark, (char) 0);
    }
}
