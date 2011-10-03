/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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

package org.yaml.snakeyaml.emitter;

import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class EmitterMultiLineTest extends TestCase {

    public void testWriteMultiLinePlain() {
        String plain = "mama\nmila\nramu";
        Yaml yaml = new Yaml();
        String output = yaml.dump(plain);
        // System.out.println(output);
        assertEquals("'mama\n\n  mila\n\n  ramu'\n", output);
        String parsed = (String) yaml.load(output);
        // System.out.println(parsed);
        assertEquals(plain, parsed);
    }

    public void testWriteMultiLineLiteral() {
        String source = "a: 1\nb: |-\n  mama\n  mila\n  ramu\n";
        // System.out.println("Source:\n" + source);
        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = (Map<String, Object>) yaml.load(source);
        String value = (String) parsed.get("b");
        // System.out.println(value);
        assertEquals("mama\nmila\nramu", value);
    }
}
