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
package org.yaml.snakeyaml;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The tag "!" must force the parser to use only the node kind (scalar, sequence, mapping)
 * (issue 459)
 */
public class ExclamationTagTest {

    @Test
    public void testImplicitTag() {
        Yaml yaml = new Yaml();
        Object result = yaml.load("! 12");
        assertEquals("It works the same way as PyYAML", 12, result);
        //It might be changed -> assertEquals("12", yaml.load("! 12"));
    }

    @Test
    public void testNoImplicitTag() {
        Yaml yaml = new Yaml();
        Object result = yaml.load("12");
        assertEquals(12, result);
    }
}
