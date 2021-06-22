/**
 * Copyright (c) 2021, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.issues.issue506;

import junit.framework.TestCase;

import java.util.Map;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.resolver.Resolver;

public class Parse0x_String extends TestCase {

    public void testFailure() {
        Yaml yaml = new Yaml();
        String input = "a: 0x_";
        Map<String, String> data = yaml.load(input);
        assertEquals("0x_", data.get("a"));
    }

    public void testRootCause() {
        Pattern regexp = Resolver.INT;
        assertTrue(regexp.matcher("0xabc").matches());
        assertFalse(regexp.matcher("0xabz").matches());
        assertFalse(regexp.matcher("0x_").matches());
    }
}
