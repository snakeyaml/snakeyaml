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

    public void testIntPattern() {
        Pattern regexp = Resolver.INT;
        assertTrue(regexp.matcher("0xabc").matches());
        assertTrue(regexp.matcher("0x_abc").matches());
        assertFalse(regexp.matcher("0x_").matches());
        assertTrue(regexp.matcher("0b_01_00").matches());
        assertFalse(regexp.matcher("0b_").matches());
        assertTrue(regexp.matcher("0_77").matches());
        assertFalse(regexp.matcher("0_").matches());
    }

    public void testFloatPattern() {
        Pattern regexp = Resolver.FLOAT;
        assertFalse(regexp.matcher("0123456789").matches());
        assertFalse(regexp.matcher("123456789").matches());

        assertTrue(regexp.matcher("00.3").matches());
        assertTrue(regexp.matcher("00.003").matches());
        assertTrue(regexp.matcher("02.003").matches());
        assertTrue(regexp.matcher("-02.003").matches());
        assertTrue(regexp.matcher("-02.003_001").matches());
        assertTrue(regexp.matcher("-2_000.003_001").matches());
        assertTrue(regexp.matcher(".3").matches());
        assertTrue(regexp.matcher("-.3").matches());
        assertTrue(regexp.matcher("+0.3").matches());
        assertTrue(regexp.matcher("8.1e-06").matches());
        assertTrue(regexp.matcher("8e-06").matches());
        assertTrue(regexp.matcher("8e06").matches());
        assertTrue(regexp.matcher("8e6").matches());
        assertTrue(regexp.matcher("8E6").matches());
        assertTrue(regexp.matcher("8E+06").matches());
        assertTrue(regexp.matcher("8e+6").matches());
        assertTrue(regexp.matcher("+8e+6").matches());
    }
}
