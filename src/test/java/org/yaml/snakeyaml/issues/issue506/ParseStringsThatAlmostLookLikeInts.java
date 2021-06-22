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

import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.resolver.Resolver;

public class ParseStringsThatAlmostLookLikeInts extends TestCase {

    public void testHexPrefixedString() {
        String in = "0x_";
        String expected = "0x_";
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testHexInt() {
        String in = "0x_AB";
        Integer expected = 171;
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testOctalPrefixedString() {
        String in = "0_";
        String expected = "0_";
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testOctalInt() {
        String in = "0_123";
        Integer expected = 83;
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testBinaryPrefixedString() {
        String in = "0b_";
        String expected = "0b_";
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testBinaryInt() {
        String in = "0b_101";
        Integer expected = 5;
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testDecimalish() {
        String in = "-_";
        String expected = "-_";
        Object out = new Yaml().load(in);
        assertEquals(expected, out);
    }

    public void testRootCause() {
        Pattern regexp = Resolver.INT;
        assertTrue(regexp.matcher("0xabc").matches());
        assertTrue(regexp.matcher("0x_abc").matches());
        assertFalse(regexp.matcher("0x_").matches());
        assertTrue(regexp.matcher("0b_01_00").matches());
        assertFalse(regexp.matcher("0b_").matches());
        assertTrue(regexp.matcher("0_77").matches());
        assertFalse(regexp.matcher("0_").matches());
    }
}
