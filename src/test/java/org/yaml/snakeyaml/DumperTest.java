/**
 * Copyright (c) 2008-2014, http://www.snakeyaml.org
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
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.resolver.Resolver;

public class DumperTest extends TestCase {

    public void testDump1() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        List<Integer> list = new ArrayList<Integer>();
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
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            list.add(i);
        }
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(list);
        assertEquals("--- [0, 1, 2]\n", output);
    }

    public void testDump3() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        List<Integer> list = new ArrayList<Integer>();
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
            throw new IOException("write test failure.");
        }
    }

    @SuppressWarnings("deprecation")
    public void testDeprecated1() {
        Yaml yaml = new Yaml(new Dumper());
        yaml.dump("aaa1234567890");
    }

    @SuppressWarnings("deprecation")
    public void testDeprecated2() {
        DumperOptions options = new DumperOptions();
        options.setCanonical(true);
        Yaml yaml = new Yaml(new Loader(), new Dumper(options), new Resolver());
        String doc = yaml.dump("aaa1234567890");
        assertEquals("---\n!!str \"aaa1234567890\"\n", doc);
    }
}
