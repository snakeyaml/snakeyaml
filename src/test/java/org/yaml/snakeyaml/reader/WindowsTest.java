/**
 * Copyright (c) 2008, SnakeYAML
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

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

/**
 * https://yaml.org/spec/1.1/#id871136
 */
public class WindowsTest extends TestCase {

    //test windows style
    public void testCRLF() {
        try {
            Yaml yaml = new Yaml();
            yaml.load("\r\n[");
        } catch (ParserException e) {
            assertTrue(e.getMessage().contains("line 2,"));
        }
    }

    public void testCRCR() {
        try {
            Yaml yaml = new Yaml();
            yaml.load("\r\r[");
        } catch (ParserException e) {
            assertTrue(e.getMessage().contains("line 3,"));
        }
    }

    //test UNIX style
    public void testLFLF() {
        try {
            Yaml yaml = new Yaml();
            yaml.load("\n\n[");
        } catch (ParserException e) {
            assertTrue(e.getMessage().contains("line 3,"));
        }
    }
}
