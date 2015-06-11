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
package org.yaml.snakeyaml.scanner;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.ValueToken;

public class ScannerImplTest extends TestCase {

    public void testGetToken() {
        String data = "string: abcd";
        StreamReader reader = new StreamReader(data);
        Scanner scanner = new ScannerImpl(reader);
        Mark dummy = new Mark("dummy", 0, 0, 0, "", 0);
        LinkedList<Token> etalonTokens = new LinkedList<Token>();
        etalonTokens.add(new StreamStartToken(dummy, dummy));
        etalonTokens.add(new BlockMappingStartToken(dummy, dummy));
        etalonTokens.add(new KeyToken(dummy, dummy));
        etalonTokens.add(new ScalarToken("string", true, dummy, dummy, (char) 0));
        etalonTokens.add(new ValueToken(dummy, dummy));
        etalonTokens.add(new ScalarToken("abcd", true, dummy, dummy, (char) 0));
        etalonTokens.add(new BlockEndToken(dummy, dummy));
        etalonTokens.add(new StreamEndToken(dummy, dummy));
        while (!etalonTokens.isEmpty() && scanner.checkToken(etalonTokens.get(0).getTokenId())) {
            assertEquals(etalonTokens.removeFirst(), scanner.getToken());
        }
        assertFalse("Must contain no more tokens: " + scanner.getToken(),
                scanner.checkToken(new Token.ID[0]));
    }

    public void testWrongTab() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("\t  data: 1");
            fail("TAB cannot start a token.");
        } catch (Exception e) {
            assertEquals(
                    "while scanning for the next token\n"
                            + "found character '\\t(TAB)' that cannot start any token. (Do not use \\t(TAB) for indentation)\n"
                            + " in 'string', line 1, column 1:\n" + "    \t  data: 1\n" + "    ^\n",
                    e.getMessage());
        }
    }
}
