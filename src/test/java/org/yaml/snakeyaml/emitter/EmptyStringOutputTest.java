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
package org.yaml.snakeyaml.emitter;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.events.DocumentStartEvent;
import org.yaml.snakeyaml.events.ImplicitTuple;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.StreamStartEvent;

import java.io.IOException;
import java.io.StringWriter;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml-engine/issues/23/emitting-only-an-empty-string-adds-to
 */
public class EmptyStringOutputTest extends TestCase {

    public void testOutputEmptyStringWithEmitter() throws IOException {
        assertEquals("The empty output must contain ---", "---", dump(""));
    }

    public void testOutputStringWithEmitter() throws IOException {
        assertEquals("The non-empty output must NOT contain ---", "v1234512345", dump("v1234512345"));
    }

    private String dump(String value) throws IOException {
        DumperOptions options = new DumperOptions();
        StringWriter output = new StringWriter();
        Emitter emitter = new Emitter(output, options);
        emitter.emit(new StreamStartEvent(null, null));
        emitter.emit(new DocumentStartEvent(null, null, false, null, null));
        emitter.emit(new ScalarEvent(null, null, new ImplicitTuple(true, false), value, null, null, DumperOptions.ScalarStyle.PLAIN));
        return output.toString();
    }
}


