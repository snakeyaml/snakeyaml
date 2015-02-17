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
package org.yaml.snakeyaml.issues.issue207;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class OctalNumberTest extends TestCase {

    public void testOctalNumbersMoreThenSeven() {
        Yaml yaml = new Yaml();
        assertEquals(7, yaml.load("07"));
        assertEquals(63, yaml.load("077"));
        assertEquals(0, yaml.load("0"));
        assertEquals("0A", yaml.load("0A"));
        assertEquals("09", yaml.load("!!str 09"));

        /* TODO fix 207 and 130
        assertEquals("08", yaml.load("08"));
        assertEquals("09", yaml.load("09"));
        Map<String, String> parsed9 = (Map<String, String>) yaml.load("a: 09");
        assertEquals("09", parsed9.get("a"));
        */
    }
}
