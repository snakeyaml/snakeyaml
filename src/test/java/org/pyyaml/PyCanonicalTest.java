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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.tokens.Token;

/**
 * @see imported from PyYAML
 */
public class PyCanonicalTest extends PyImportTest {

    public void testCanonicalScanner() throws IOException {
        File[] files = getStreamsByExtension(".canonical");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            InputStream input = new FileInputStream(files[i]);
            List<Token> tokens = canonicalScan(input);
            input.close();
            assertFalse(tokens.isEmpty());
        }
    }

    private List<Token> canonicalScan(InputStream input) throws IOException {
        int ch = input.read();
        StringBuilder buffer = new StringBuilder();
        while (ch != -1) {
            buffer.append((char) ch);
            ch = input.read();
        }
        CanonicalScanner scanner = new CanonicalScanner(buffer.toString());
        List<Token> result = new ArrayList<Token>();
        while (scanner.peekToken() != null) {
            result.add(scanner.getToken());
        }
        return result;
    }

    public void testCanonicalParser() throws IOException {
        File[] files = getStreamsByExtension(".canonical");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            InputStream input = new FileInputStream(files[i]);
            List<Event> tokens = canonicalParse(input);
            input.close();
            assertFalse(tokens.isEmpty());
        }
    }
}
