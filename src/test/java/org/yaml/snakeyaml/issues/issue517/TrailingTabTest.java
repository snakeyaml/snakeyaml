/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue517;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

public class TrailingTabTest extends TestCase {

    public void testTrailingTab() {
        String str = "'bar'\t";
        Yaml yaml = new Yaml();
        try {
            Object obj = yaml.load(str);
            fail("Issue 517"); //TODO FIXME trailing TAB should be ignored
            assertNotNull(obj);
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("found character '\\t(TAB)' that cannot start any token."));
        }
    }
}
