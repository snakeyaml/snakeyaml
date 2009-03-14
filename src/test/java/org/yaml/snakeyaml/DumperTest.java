package org.yaml.snakeyaml;

/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class DumperTest extends TestCase {

    public void testDump1() {
        DumperOptions options = new DumperOptions();
        options.setDefaultStyle(DumperOptions.DefaultScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(list);
        assertEquals("---\n- !!int \"0\"\n- !!int \"1\"\n- !!int \"2\"\n...\n", output);
    }

    public void testDump2() {
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(list);
        assertEquals("--- [0, 1, 2]\n", output);
    }

    public void testDump3() {
        DumperOptions options = new DumperOptions();
        options.setDefaultStyle(DumperOptions.DefaultScalarStyle.SINGLE_QUOTED);
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(list);
        assertEquals("- !!int '0'\n- !!int '1'\n- !!int '2'\n", output);
    }

    public void testDumpException() {
        Yaml yaml = new Yaml();
        Writer writer = new ExceptionWriter1();
        try {
            yaml.dump("aaa1234567890", writer);
            fail("Exception must be thrown.");
        } catch (Exception e) {
            assertEquals("java.io.IOException: write test failure.", e.getMessage());
        }
    }

    private class ExceptionWriter1 extends Writer {
        @Override
        public void write(String str) throws IOException {
            throw new IOException("write test failure.");
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
        }
    }
}
