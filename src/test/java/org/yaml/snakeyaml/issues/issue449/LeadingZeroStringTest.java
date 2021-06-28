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
package org.yaml.snakeyaml.issues.issue449;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.resolver.Resolver;

import java.util.regex.Pattern;

public class LeadingZeroStringTest extends TestCase {

    public void testString() {
        Yaml loader = new Yaml();
        // this almost looks like an octal, but it contains digits greater than 7, so it's a string
        Object result = loader.load("0123456789");
        assertEquals("0123456789", result);
    }

    public void testLeadingZeroForIntIsAccepted() {
        Pattern regexp = Resolver.INT;
        assertTrue("Valid octal must be recognised.", regexp.matcher("07").matches());
    }

    public void testOctalNumberCannotHave8() {
        Pattern regexp = Resolver.INT;
        assertFalse(regexp.matcher("08").matches());
        assertFalse(regexp.matcher("0123456789").matches());
    }
}
