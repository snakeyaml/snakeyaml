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
package org.yaml.snakeyaml.issues.issue500;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

import java.util.Iterator;

public class FailIteratorOnlyWhenErrorFoundTest extends TestCase {

    public void testFailure() {
        try {
            Yaml yamlProcessor = new Yaml();
            String data = "a: 1\n" +
                    "---\n" +
                    "Some comment \n" +
                    "\n" +
                    "\n" +
                    "b: 2\n" +
                    "\n" +
                    "c: 3\n" +
                    "---";
            Iterable<Object> parsed = yamlProcessor.loadAll(data);
            Iterator<Object> iterator = parsed.iterator();
            assertNotNull(iterator.next()); //no failure, first document id valid
            iterator.next();
//            for (Object obj : parsed) {
//                assertNotNull(obj);
//                System.out.println(obj);
//            }
            fail("Should not accept the second document");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("in 'reader', line 6, column 2:"));
        }
    }
}
