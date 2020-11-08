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
package org.yaml.snakeyaml.issues.issue468;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class NonAsciiAnchorTest extends TestCase {
    private final String NON_ANCHORS = ":,[]{}*&./";

    public void testNonAsciiAnchor() {
        Yaml loader = new Yaml();
        String value = loader.load("&something_タスク タスク");
        assertEquals("タスク", value);
    }

    public void testUnderscore() {
        Yaml loader = new Yaml();
        String value = loader.load("&_ タスク");
        assertEquals("タスク", value);
    }

    public void testSmile() {
        Yaml loader = new Yaml();
        String value = loader.load("&\uD83D\uDE01 v1");
        //System.out.println("&\uD83D\uDE01 v1");
        assertEquals("v1", value);
    }

    public void testAlpha() {
        Yaml loader = new Yaml();
        String value = loader.load("&kääk v1");
        assertEquals("v1", value);
    }

    public void testNonAllowedAnchor() {
        for (int i = 0; i < NON_ANCHORS.length(); i++) {
            try {
                Character c = NON_ANCHORS.charAt(i);
                loadWith(c);
                fail("Special chars should not be allowed in anchor name: " + c);
            } catch (Exception e) {
                assertTrue(e.getMessage(), e.getMessage().contains("while scanning an anchor"));
                assertTrue(e.getMessage(), e.getMessage().contains("unexpected character found"));
            }
        }
    }

    private void loadWith(char c) {
        Yaml loader = new Yaml();
        loader.load("&" + c + " value");
    }
}
