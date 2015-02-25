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

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.ReaderException;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 * @see imported from PyYAML
 */
public class PyReaderTest extends PyImportTest {

    public void testReaderUnicodeErrors() throws IOException {
        File[] inputs = getStreamsByExtension(".stream-error");
        for (int i = 0; i < inputs.length; i++) {
            InputStream input = new FileInputStream(inputs[i]);
            StreamReader stream = new StreamReader(new UnicodeReader(input));
            try {
                while (stream.peek() != '\u0000') {
                    stream.forward();
                }
                fail("Invalid stream must not be accepted: " + inputs[i].getAbsolutePath()
                        + "; encoding=" + stream.getEncoding());
            } catch (ReaderException e) {
                assertTrue(e.toString(),
                        e.toString().contains(" special characters are not allowed"));
            } catch (YAMLException e) {
                assertTrue(e.toString(), e.toString().contains("MalformedInputException"));
            } finally {
                input.close();
            }
        }
    }
}
