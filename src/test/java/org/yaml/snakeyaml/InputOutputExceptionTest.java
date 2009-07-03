/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.error.YAMLException;

public class InputOutputExceptionTest extends TestCase {
    public void testIOExceptionOnLoad() throws Exception {
        try {
            new Yaml().load(new BrokenInputStream());
            fail("Input must be broken.");
        } catch (YAMLException e) {
            assertTrue(e.getCause() instanceof IOException);
            assertEquals("java.io.IOException: Broken 2", e.getMessage());
        }
    }

    public void testIOExceptionOnDump() throws Exception {
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
