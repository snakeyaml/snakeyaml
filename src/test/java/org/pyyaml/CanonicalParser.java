/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package org.pyyaml;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.yaml.snakeyaml.events.AliasEvent;
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
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
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

public class CanonicalParser implements Parser {
    private LinkedList<Event> events;
    private boolean parsed;
    private CanonicalScanner scanner;

    public CanonicalParser(String data) {
        events = new LinkedList<Event>();
        parsed = false;
        scanner = new CanonicalScanner(data);
    }

    // stream: STREAM-START document* STREAM-END
    private void parseStream() {
        scanner.getToken(StreamStartToken.class);
        events.add(new StreamStartEvent(null, null));
        while (!scanner.checkToken(StreamEndToken.class)) {
            List<Class<? extends Token>> list = new ArrayList<Class<? extends Token>>();
            list.add(DirectiveToken.class);
            list.add(DocumentStartToken.class);
            if (scanner.checkToken(list)) {
                parseDocument();
            } else {
                throw new CanonicalException("document is expected, got " + scanner.tokens.get(0));
            }
        }
        scanner.getToken(StreamEndToken.class);
        events.add(new StreamEndEvent(null, null));
    }

    // document: DIRECTIVE? DOCUMENT-START node
    private void parseDocument() {
        if (scanner.checkToken(DirectiveToken.class)) {
            scanner.getToken(DirectiveToken.class);
        }
        scanner.getToken(DocumentStartToken.class);
        events.add(new DocumentStartEvent(null, null, true, new Integer[] { 1, 1 }, null));
        parseNode();
        events.add(new DocumentEndEvent(null, null, true));
    }

    // node: ALIAS | ANCHOR? TAG? (SCALAR|sequence|mapping)
    private void parseNode() {
        if (scanner.checkToken(AliasToken.class)) {
            AliasToken token = (AliasToken) scanner.getToken();
            events.add(new AliasEvent(token.getValue(), null, null));
        } else {
            String anchor = null;
            if (scanner.checkToken(AnchorToken.class)) {
                AnchorToken token = (AnchorToken) scanner.getToken();
                anchor = token.getValue();
            }
            String tag = null;
            if (scanner.checkToken(TagToken.class)) {
                TagToken token = (TagToken) scanner.getToken();
                tag = token.getValue()[0] + token.getValue()[1];
            }
            if (scanner.checkToken(ScalarToken.class)) {
                ScalarToken token = (ScalarToken) scanner.getToken();
                events.add(new ScalarEvent(anchor, tag, new ImplicitTuple(false, false), token
                        .getValue(), null, null, null));
            } else if (scanner.checkToken(FlowSequenceStartToken.class)) {
                events.add(new SequenceStartEvent(anchor, tag, false, null, null, null));
                parseSequence();
            } else if (scanner.checkToken(FlowMappingStartToken.class)) {
                events.add(new MappingStartEvent(anchor, tag, false, null, null, null));
                parseMapping();
            } else {
                throw new CanonicalException("SCALAR, '[', or '{' is expected, got "
                        + scanner.tokens.get(0));
            }
        }
    }

    // sequence: SEQUENCE-START (node (ENTRY node)*)? ENTRY? SEQUENCE-END
    private void parseSequence() {
        scanner.getToken(FlowSequenceStartToken.class);
        if (!scanner.checkToken(FlowSequenceEndToken.class)) {
            parseNode();
            while (!scanner.checkToken(FlowSequenceEndToken.class)) {
                scanner.getToken(FlowEntryToken.class);
                if (!scanner.checkToken(FlowSequenceEndToken.class)) {
                    parseNode();
                }
            }
        }
        scanner.getToken(FlowSequenceEndToken.class);
        events.add(new SequenceEndEvent(null, null));
    }

    // mapping: MAPPING-START (map_entry (ENTRY map_entry)*)? ENTRY? MAPPING-END
    private void parseMapping() {
        scanner.getToken(FlowMappingStartToken.class);
        if (!scanner.checkToken(FlowMappingEndToken.class)) {
            parseMapEntry();
            while (!scanner.checkToken(FlowMappingEndToken.class)) {
                scanner.getToken(FlowEntryToken.class);
                if (!scanner.checkToken(FlowMappingEndToken.class)) {
                    parseMapEntry();
                }
            }
        }
        scanner.getToken(FlowMappingEndToken.class);
        events.add(new MappingEndEvent(null, null));
    }

    // map_entry: KEY node VALUE node
    private void parseMapEntry() {
        scanner.getToken(KeyToken.class);
        parseNode();
        scanner.getToken(ValueToken.class);
        parseNode();
    }

    public void parse() {
        parseStream();
        parsed = true;
    }

    public Event getEvent() {
        if (!parsed) {
            parse();
        }
        return events.removeFirst();
    }

    public boolean checkEvent(List<Class<? extends Event>> choices) {
        if (!parsed) {
            parse();
        }
        if (!events.isEmpty()) {
            if (choices.isEmpty()) {
                return true;
            }
            for (Class<? extends Event> class1 : choices) {
                if (class1.isInstance(events.peek())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkEvent(Class<? extends Event> choice) {
        List<Class<? extends Event>> list = new ArrayList<Class<? extends Event>>(1);
        list.add(choice);
        return checkEvent(list);
    }

    public Event peekEvent() {
        if (!parsed) {
            parse();
        }
        if (events.isEmpty()) {
            return null;
        } else {
            return events.get(0);
        }
    }

}
