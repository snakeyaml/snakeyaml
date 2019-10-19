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
package org.yaml.snakeyaml;

import junit.framework.TestCase;

/**
 * The tag "!" must force the parser to use only the node kind (scalar, sequence, mapping)
 * (issue 459)
 */
public class ExclamationTagTest extends TestCase {

    public void testImplicitTag() {
        Yaml yaml = new Yaml();
        assertEquals("It works the same way as PyYAML",12, yaml.load("! 12"));
        //It might be changed -> assertEquals("12", yaml.load("! 12"));
    }

    public void testNoImplicitTag() {
        Yaml yaml = new Yaml();
        assertEquals(12, yaml.load("12"));
    }
}
