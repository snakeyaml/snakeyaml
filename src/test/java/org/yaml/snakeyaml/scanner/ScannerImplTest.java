/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.scanner;

import java.util.ArrayList;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.ValueToken;

public class ScannerImplTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testGetToken() {
        String data = "string: abcd";
        Reader reader = new Reader(data);
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
        while (scanner.checkToken(new ArrayList())) {
            assertEquals(etalonTokens.removeFirst(), scanner.getToken());
            // System.out.println(scanner.getToken());
        }
        assertFalse("Must contain no more tokens: " + scanner.getToken(), scanner
                .checkToken(new ArrayList()));
    }
}
