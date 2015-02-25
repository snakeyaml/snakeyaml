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
package org.yaml.snakeyaml.parser;

import java.util.HashMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.Version;

public class VersionTagsTupleTest extends TestCase {

    public void testToString() {
        VersionTagsTuple tuple = new VersionTagsTuple(Version.V1_1, new HashMap<String, String>());
        assertEquals("VersionTagsTuple<Version: 1.1, {}>", tuple.toString());
    }
}
