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
package org.yaml.snakeyaml.issues.issue151;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class EscapedUnicodeTest extends TestCase {

    public void testUnicode() {
        Yaml yaml = new Yaml();
        // http://www.tutorialspoint.com/html/ascii_table_lookup.htm
        String str = (String) yaml.load("\"\\xC3\\xA4\"");
        assertEquals("2 escape sequences must be converted to 2 characters.", "Ã¤", str);
    }

    public void testUnicode2() {
        Yaml yaml = new Yaml();
        String str = (String) yaml.load("\"Acetylsalicyls\\xE4ure\"");
        assertEquals("E4 -> ä", "Acetylsalicylsäure", str);
    }
}
