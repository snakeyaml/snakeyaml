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
package org.yaml.snakeyaml.issues.issue397;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

import java.util.List;

public class ColonInFlowContextInListTest extends TestCase {

    private Yaml loader = new Yaml();

    public void testList() {
        List<String> list = (List<String>) loader.load("[ http://foo ]");
        assertTrue(list.contains("http://foo"));
    }

    public void testListNoSpaces() {
        List<String> list = (List<String>) loader.load("[http://foo]");
        assertTrue(list.contains("http://foo"));
    }

    public void testList2() {
        List<String> list = (List<String>) loader.load("[ http://foo,http://bar ]");
        assertTrue(list.contains("http://foo"));
        assertTrue(list.contains("http://bar"));
    }
}
