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

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.YAMLException;

public class InputOutputExceptionTest extends TestCase {
    public void testIOExceptionOnLoad() {
        try {
            new Yaml().load(new BrokenInputStream());
            fail("Input must be broken.");
        } catch (YAMLException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("java.io.IOException: Broken 2", e.getMessage());
        }
    }

    public void testIOExceptionOnDump() {
        try {
            new Yaml().dump("something", new BrokenWriter());
            fail("Output must be broken.");
        } catch (YAMLException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("java.io.IOException: Broken 12", e.getMessage());
        }
    }

    private static class BrokenInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException("Broken 1");
        }

        @Override
        public int read(byte[] bytes, int i, int i1) throws IOException {
            throw new IOException("Broken 2");
        }

        @Override
        public void close() throws IOException {
            throw new IOException("Broken 3");
        }
    }

    private static class BrokenWriter extends Writer {
        @Override
        public void close() throws IOException {
            throw new IOException("Broken 10");
        }

        @Override
        public void flush() throws IOException {
            throw new IOException("Broken 11");
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("Broken 12");
        }
    }
}
