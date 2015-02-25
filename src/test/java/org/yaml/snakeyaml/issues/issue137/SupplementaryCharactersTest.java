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
package org.yaml.snakeyaml.issues.issue137;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * http://java.sun.com/developer/technicalArticles/Intl/Supplementary/
 */
public class SupplementaryCharactersTest extends TestCase {

    public void testSupplementaryCharacter() {
        Yaml yaml = new Yaml();
        String parsed = (String) yaml.load("\"\\U0001f648\"");
        assertEquals("\ud83d\ude48", parsed);
        // System.out.println(data);
    }

    public void testBasicMultilingualPlane() {
        Yaml yaml = new Yaml();
        String parsed = (String) yaml.load("\"\\U00000041\"");
        assertEquals("A", parsed);
    }

    /**
     * Supplementary Characters are dumped as binary
     */
    public void testDumpSupplementaryCharacter() throws UnsupportedEncodingException {
        String supplementary = "\ud83d\ude48";
        Yaml yaml = new Yaml();
        String output = yaml.dump(supplementary);
        assertEquals("!!binary |-\n  8J+ZiA==\n", output);
        byte[] binary = (byte[]) yaml.load(output);
        String binString = new String(binary, "UTF-8");
        assertEquals(supplementary, binString);
    }

    public void testLoadSupplementaryCharacter() {
        try {
            new Yaml().load("\"\ud83d\ude48\"\n");
            fail("Are Supplementary Characters printable ?");
        } catch (Exception e) {
            assertEquals("special characters are not allowed", e.getMessage());
        }
    }
}
