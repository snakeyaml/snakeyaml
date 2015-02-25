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
package org.yaml.snakeyaml.issues.issue163;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class LinearScalaTralingTest extends TestCase {

    public void testClipChomping() throws Exception {
        String data = "testnode: |\n   This is line 1\n   This is line 2\n";
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, String> payload = (Map<String, String>) yaml.load(data);
        assertEquals("This is line 1\nThis is line 2\n", payload.get("testnode"));
    }

    public void testStripChomping() throws Exception {
        // mind the trailing '|-' to indicate strip chomping
        String data = "testnode: |-\n   This is line 1\n   This is line 2\n";
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, String> payload = (Map<String, String>) yaml.load(data);
        assertEquals("No traling line break expected.", "This is line 1\nThis is line 2",
                payload.get("testnode"));
    }
}
