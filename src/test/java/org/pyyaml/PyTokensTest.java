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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.reader.UnicodeReader;
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
 * @see imported from PyYAML
 */
public class PyTokensTest extends PyImportTest {

    public void testTokens() throws FileNotFoundException {
        Map<Class<?>, String> replaces = new HashMap<Class<?>, String>();
        replaces.put(DirectiveToken.class, "%");
        replaces.put(DocumentStartToken.class, "---");
        replaces.put(DocumentEndToken.class, "...");
        replaces.put(AliasToken.class, "*");
        replaces.put(AnchorToken.class, "&");
        replaces.put(TagToken.class, "!");
        replaces.put(ScalarToken.class, "_");
        replaces.put(BlockSequenceStartToken.class, "[[");
        replaces.put(BlockMappingStartToken.class, "{{");
        replaces.put(BlockEndToken.class, "]}");
        replaces.put(FlowSequenceStartToken.class, "[");
        replaces.put(FlowSequenceEndToken.class, "]");
        replaces.put(FlowMappingStartToken.class, "{");
        replaces.put(FlowMappingEndToken.class, "}");
        replaces.put(BlockEntryToken.class, ",");
        replaces.put(FlowEntryToken.class, ",");
        replaces.put(KeyToken.class, "?");
        replaces.put(ValueToken.class, ":");
        //
        File[] tokensFiles = getStreamsByExtension(".tokens");
        assertTrue("No test files found.", tokensFiles.length > 0);
        for (int i = 0; i < tokensFiles.length; i++) {
            String name = tokensFiles[i].getName();
            int position = name.lastIndexOf('.');
            String dataName = name.substring(0, position) + ".data";
            //
            String tokenFileData = getResource(name);
            String[] split = tokenFileData.split("\\s+");
            List<String> tokens2 = new ArrayList<String>();
            for (int j = 0; j < split.length; j++) {
                tokens2.add(split[j]);
            }
            //
            List<String> tokens1 = new ArrayList<String>();
            StreamReader reader = new StreamReader(new UnicodeReader(new FileInputStream(
                    getFileByName(dataName))));
            Scanner scanner = new ScannerImpl(reader);
            try {
                while (scanner.checkToken(new Token.ID[0])) {
                    Token token = scanner.getToken();
                    if (!(token instanceof StreamStartToken || token instanceof StreamEndToken)) {
                        String replacement = replaces.get(token.getClass());
                        tokens1.add(replacement);
                    }
                }
                assertEquals(tokenFileData, tokens1.size(), tokens2.size());
                assertEquals(tokens1, tokens2);
            } catch (RuntimeException e) {
                System.out.println("File name: \n" + tokensFiles[i].getName());
                String data = getResource(tokensFiles[i].getName());
                System.out.println("Data: \n" + data);
                System.out.println("Tokens:");
                for (String token : tokens1) {
                    System.out.println(token);
                }
                fail("Cannot scan: " + tokensFiles[i]);
            }
        }
    }

    public void testScanner() throws IOException {
        File[] files = getStreamsByExtension(".data", true);
        assertTrue("No test files found.", files.length > 0);
        for (File file : files) {
            List<String> tokens = new ArrayList<String>();
            InputStream input = new FileInputStream(file);
            StreamReader reader = new StreamReader(new UnicodeReader(input));
            Scanner scanner = new ScannerImpl(reader);
            try {
                while (scanner.checkToken(new Token.ID[0])) {
                    Token token = scanner.getToken();
                    tokens.add(token.getClass().getName());
                }
            } catch (RuntimeException e) {
                System.out.println("File name: \n" + file.getName());
                String data = getResource(file.getName());
                System.out.println("Data: \n" + data);
                System.out.println("Tokens:");
                for (String token : tokens) {
                    System.out.println(token);
                }
                fail("Cannot scan: " + file + "; " + e.getMessage());
            } finally {
                input.close();
            }
        }
    }
}
