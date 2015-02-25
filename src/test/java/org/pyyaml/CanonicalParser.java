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
package org.pyyaml;

import java.util.ArrayList;

import org.yaml.snakeyaml.DumperOptions.Version;
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
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.Token;

public class CanonicalParser implements Parser {
    private ArrayList<Event> events;
    private boolean parsed;
    private CanonicalScanner scanner;

    public CanonicalParser(String data) {
        events = new ArrayList<Event>();
        parsed = false;
        scanner = new CanonicalScanner(data);
    }

    // stream: STREAM-START document* STREAM-END
    private void parseStream() {
        scanner.getToken(Token.ID.StreamStart);
        events.add(new StreamStartEvent(null, null));
        while (!scanner.checkToken(Token.ID.StreamEnd)) {
            if (scanner.checkToken(Token.ID.Directive, Token.ID.DocumentStart)) {
                parseDocument();
            } else {
                throw new CanonicalException("document is expected, got " + scanner.tokens.get(0));
            }
        }
        scanner.getToken(Token.ID.StreamEnd);
        events.add(new StreamEndEvent(null, null));
    }

    // document: DIRECTIVE? DOCUMENT-START node
    private void parseDocument() {
        if (scanner.checkToken(Token.ID.Directive)) {
            scanner.getToken(Token.ID.Directive);
        }
        scanner.getToken(Token.ID.DocumentStart);
        events.add(new DocumentStartEvent(null, null, true, Version.V1_1, null));
        parseNode();
        events.add(new DocumentEndEvent(null, null, true));
    }

    // node: ALIAS | ANCHOR? TAG? (SCALAR|sequence|mapping)
    private void parseNode() {
        if (scanner.checkToken(Token.ID.Alias)) {
            AliasToken token = (AliasToken) scanner.getToken();
            events.add(new AliasEvent(token.getValue(), null, null));
        } else {
            String anchor = null;
            if (scanner.checkToken(Token.ID.Anchor)) {
                AnchorToken token = (AnchorToken) scanner.getToken();
                anchor = token.getValue();
            }
            String tag = null;
            if (scanner.checkToken(Token.ID.Tag)) {
                TagToken token = (TagToken) scanner.getToken();
                tag = token.getValue().getHandle() + token.getValue().getSuffix();
            }
            if (scanner.checkToken(Token.ID.Scalar)) {
                ScalarToken token = (ScalarToken) scanner.getToken();
                events.add(new ScalarEvent(anchor, tag, new ImplicitTuple(false, false), token
                        .getValue(), null, null, null));
            } else if (scanner.checkToken(Token.ID.FlowSequenceStart)) {
                events.add(new SequenceStartEvent(anchor, tag, false, null, null, null));
                parseSequence();
            } else if (scanner.checkToken(Token.ID.FlowMappingStart)) {
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
        scanner.getToken(Token.ID.FlowSequenceStart);
        if (!scanner.checkToken(Token.ID.FlowSequenceEnd)) {
            parseNode();
            while (!scanner.checkToken(Token.ID.FlowSequenceEnd)) {
                scanner.getToken(Token.ID.FlowEntry);
                if (!scanner.checkToken(Token.ID.FlowSequenceEnd)) {
                    parseNode();
                }
            }
        }
        scanner.getToken(Token.ID.FlowSequenceEnd);
        events.add(new SequenceEndEvent(null, null));
    }

    // mapping: MAPPING-START (map_entry (ENTRY map_entry)*)? ENTRY? MAPPING-END
    private void parseMapping() {
        scanner.getToken(Token.ID.FlowMappingStart);
        if (!scanner.checkToken(Token.ID.FlowMappingEnd)) {
            parseMapEntry();
            while (!scanner.checkToken(Token.ID.FlowMappingEnd)) {
                scanner.getToken(Token.ID.FlowEntry);
                if (!scanner.checkToken(Token.ID.FlowMappingEnd)) {
                    parseMapEntry();
                }
            }
        }
        scanner.getToken(Token.ID.FlowMappingEnd);
        events.add(new MappingEndEvent(null, null));
    }

    // map_entry: KEY node VALUE node
    private void parseMapEntry() {
        scanner.getToken(Token.ID.Key);
        parseNode();
        scanner.getToken(Token.ID.Value);
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
        return events.remove(0);
    }

    /**
     * Check the type of the next event.
     */
    public boolean checkEvent(Event.ID choice) {
        if (!parsed) {
            parse();
        }
        if (!events.isEmpty()) {
            if (events.get(0).is(choice)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the next event.
     */
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
