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
package org.yaml.snakeyaml.issues.issue116;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class NoFieldsTest extends TestCase {

    public void testEmptyClass() {
        Empty empty = new Empty();
        Yaml yaml = new Yaml();
        String result = yaml.dump(empty);
        assertEquals("!!org.yaml.snakeyaml.issues.issue116.Empty {}\n", result);
        Object emptyParsed = yaml.load(result);
        assertTrue(emptyParsed instanceof Empty);
    }

    public void testHiddenParameter() {
        Hidden hidden = new Hidden();
        Yaml yaml = new Yaml();
        try {
            yaml.dump(hidden);
            fail("an exception should have been thrown");
        } catch (YAMLException e) {
            assertEquals(e.getMessage(),
                    "No JavaBean properties found in org.yaml.snakeyaml.issues.issue116.Hidden");
        }
        Object hiddenParsed = yaml.load("!!org.yaml.snakeyaml.issues.issue116.Hidden {}\n");
        assertTrue(hiddenParsed instanceof Hidden);
    }

    public void testSpecialHiddenParameter() {
        HiddenSpecial hidden = new HiddenSpecial("qwerty");
        Yaml yaml = new Yaml();
        try {
            yaml.dump(hidden);
            fail("an exception should have been thrown");
        } catch (YAMLException e) {
            assertEquals(e.getMessage(),
                    "No JavaBean properties found in org.yaml.snakeyaml.issues.issue116.HiddenSpecial");
        }
        HiddenSpecial hs = (HiddenSpecial) yaml
                .load("!!org.yaml.snakeyaml.issues.issue116.HiddenSpecial foo\n");
        assertEquals("foo".hashCode(), hs.retrieveMyVerySpecialField());
    }
}

class Empty {
}

class Hidden {
    @SuppressWarnings("unused")
    private int inaccessableField;
}
