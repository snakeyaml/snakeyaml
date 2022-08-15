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
package org.yaml.snakeyaml.issues.issue145;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class LineNumberInExceptionTest extends TestCase {

    public void testLineReport() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("---\n!!org.yaml.snakeyaml.issues.issue145.AbstractThing { id: QQQ }");
            fail("Instances for abstract classes cannot be created");
        } catch (Exception e) {
            assertTrue(e.toString().contains("line 2, column 1"));

            String tag = "tag:yaml.org,2002:org.yaml.snakeyaml.issues.issue145.AbstractThing";
            String expectedError = String.format("Can't create an instance for %s\n" +
                                                 " in 'string', line 2, column 1:\n" +
                                                 "    !!org.yaml.snakeyaml.issues.issu ... \n" +
                                                 "    ^\n", tag);

            assertEquals(expectedError, e.getMessage());
        }
    }

    public void testCompleteThing() {
        Yaml yaml = new Yaml();
        CompleteThing thing = (CompleteThing) yaml
                .load("---\n!!org.yaml.snakeyaml.issues.issue145.CompleteThing { id: QQQ }");
        assertEquals("QQQ", thing.getId());
    }

    public void testWrongParameter() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("---\n!!org.yaml.snakeyaml.issues.issue145.CompleteThing { id2: QQQ }");
            fail("Invalid parameter");
        } catch (Exception e) {
            assertTrue("The error should ponit to QQQ.", e.toString().contains("line 2, column 59"));
        }
    }
}
