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
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.scanner.Scanner;
import org.yaml.snakeyaml.scanner.ScannerImpl;
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
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.ValueToken;

public class CanonicalScanner implements Scanner {
    private static final String DIRECTIVE = "%YAML 1.1";
    private final static Map<Character, Integer> QUOTE_CODES = ScannerImpl.ESCAPE_CODES;

    private final static Map<Character, String> QUOTE_REPLACES = ScannerImpl.ESCAPE_REPLACEMENTS;

    private String data;
    private int index;
    public ArrayList<Token> tokens;
    private boolean scanned;
    private Mark mark;

    public CanonicalScanner(String data) {
        this.data = data + "\0";
        this.index = 0;
        this.tokens = new ArrayList<Token>();
        this.scanned = false;
        this.mark = new Mark("test", 0, 0, 0, data, 0);
    }

    public boolean checkToken(Token.ID... choices) {
        if (!scanned) {
            scan();
        }
        if (!tokens.isEmpty()) {
            if (choices.length == 0) {
                return true;
            }
            Token first = this.tokens.get(0);
            for (Token.ID choice : choices) {
                if (first.getTokenId() == choice) {
                    return true;
                }
            }
        }
        return false;
    }

    public Token peekToken() {
        if (!scanned) {
            scan();
        }
        if (!tokens.isEmpty()) {
            return this.tokens.get(0);
        }
        return null;
    }

    public Token getToken() {
        if (!scanned) {
            scan();
        }
        return this.tokens.remove(0);
    }

    public Token getToken(Token.ID choice) {
        Token token = getToken();
        if (choice != null && token.getTokenId() != choice) {
            throw new CanonicalException("unexpected token " + token);
        }
        return token;
    }

    private void scan() {
        this.tokens.add(new StreamStartToken(mark, mark));
        boolean stop = false;
        while (!stop) {
            findToken();
            char ch = data.charAt(index);
            switch (ch) {
            case '\0':
                tokens.add(new StreamEndToken(mark, mark));
                stop = true;
                break;

            case '%':
                tokens.add(scanDirective());
                break;

            case '-':
                if ("---".equals(data.substring(index, index + 3))) {
                    index += 3;
                    tokens.add(new DocumentStartToken(mark, mark));
                }
                break;

            case '[':
                index++;
                tokens.add(new FlowSequenceStartToken(mark, mark));
                break;

            case '{':
                index++;
                tokens.add(new FlowMappingStartToken(mark, mark));
                break;

            case ']':
                index++;
                tokens.add(new FlowSequenceEndToken(mark, mark));
                break;

            case '}':
                index++;
                tokens.add(new FlowMappingEndToken(mark, mark));
                break;

            case '?':
                index++;
                tokens.add(new KeyToken(mark, mark));
                break;

            case ':':
                index++;
                tokens.add(new ValueToken(mark, mark));
                break;

            case ',':
                index++;
                tokens.add(new FlowEntryToken(mark, mark));
                break;

            case '*':
                tokens.add(scanAlias());
                break;

            case '&':
                tokens.add(scanAlias());
                break;

            case '!':
                tokens.add(scanTag());
                break;

            case '"':
                tokens.add(scanScalar());
                break;

            default:
                throw new CanonicalException("invalid token");
            }
        }
        scanned = true;
    }

    private Token scanDirective() {
        String chunk1 = data.substring(index, index + DIRECTIVE.length());
        char chunk2 = data.charAt(index + DIRECTIVE.length());
        if (DIRECTIVE.equals(chunk1) && "\n\0".indexOf(chunk2) != -1) {
            index += DIRECTIVE.length();
            List<Integer> implicit = new ArrayList<Integer>(2);
            implicit.add(new Integer(1));
            implicit.add(new Integer(1));
            return new DirectiveToken<Integer>("YAML", implicit, mark, mark);
        } else {
            throw new CanonicalException("invalid directive");
        }
    }

    private Token scanAlias() {
        boolean isTokenClassAlias;
        if (data.charAt(index) == '*') {
            isTokenClassAlias = true;
        } else {
            isTokenClassAlias = false;
        }
        index++;
        int start = index;
        while (", \n\0".indexOf(data.charAt(index)) == -1) {
            index++;
        }
        String value = data.substring(start, index);
        Token token;
        if (isTokenClassAlias) {
            token = new AliasToken(value, mark, mark);
        } else {
            token = new AnchorToken(value, mark, mark);
        }
        return token;
    }

    private Token scanTag() {
        index++;
        int start = index;
        while (" \n\0".indexOf(data.charAt(index)) == -1) {
            index++;
        }
        String value = data.substring(start, index);
        if (value.length() == 0) {
            value = "!";
        } else if (value.charAt(0) == '!') {
            value = Tag.PREFIX + value.substring(1);
        } else if (value.charAt(0) == '<' && value.charAt(value.length() - 1) == '>') {
            value = value.substring(1, value.length() - 1);
        } else {
            value = "!" + value;
        }
        return new TagToken(new TagTuple("", value), mark, mark);
    }

    private Token scanScalar() {
        index++;
        StringBuilder chunks = new StringBuilder();
        int start = index;
        boolean ignoreSpaces = false;
        while (data.charAt(index) != '"') {
            if (data.charAt(index) == '\\') {
                ignoreSpaces = false;
                chunks.append(data.substring(start, index));
                index++;
                char ch = data.charAt(index);
                index++;
                if (ch == '\n') {
                    ignoreSpaces = true;
                } else if (QUOTE_CODES.keySet().contains(ch)) {
                    int length = QUOTE_CODES.get(ch);
                    int code = Integer.parseInt(data.substring(index, index + length), 16);
                    chunks.append(String.valueOf((char) code));
                    index += length;
                } else {
                    if (!QUOTE_REPLACES.keySet().contains(ch)) {
                        throw new CanonicalException("invalid escape code");
                    }
                    chunks.append(QUOTE_REPLACES.get(ch));
                }
                start = index;
            } else if (data.charAt(index) == '\n') {
                chunks.append(data.substring(start, index));
                chunks.append(" ");
                index++;
                start = index;
                ignoreSpaces = true;
            } else if (ignoreSpaces && data.charAt(index) == ' ') {
                index++;
                start = index;
            } else {
                ignoreSpaces = false;
                index++;
            }
        }
        chunks.append(data.substring(start, index));
        index++;
        return new ScalarToken(chunks.toString(), mark, mark, false);
    }

    private void findToken() {
        boolean found = false;
        while (!found) {
            while (" \t".indexOf(data.charAt(index)) != -1) {
                index++;
            }
            if (data.charAt(index) == '#') {
                while (data.charAt(index) != '\n') {
                    index++;
                }
            }
            if (data.charAt(index) == '\n') {
                index++;
            } else {
                found = true;
            }
        }
    }
}
