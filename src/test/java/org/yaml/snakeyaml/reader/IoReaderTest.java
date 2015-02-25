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
package org.yaml.snakeyaml.reader;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class IoReaderTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testCheckPrintable() throws IOException {
        Yaml yaml = new Yaml();
        Reader reader = new FileReader("src/test/resources/specification/example2_1.yaml");
        List<String> list = (List<String>) yaml.load(reader);
        reader.close();
        assertEquals(3, list.size());
    }

    /**
     * test input which is longer then internal buffer - 1k
     */
    public void testBigInput() throws IOException {
        Yaml yaml = new Yaml();
        Reader reader = new FileReader("src/test/resources/reader/large.yaml");
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) yaml.load(reader);
        reader.close();
        assertEquals(37, list.size());
    }
}
