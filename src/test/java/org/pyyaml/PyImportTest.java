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
package org.pyyaml;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.reader.UnicodeReader;

public abstract class PyImportTest extends TestCase {
    public static final String PATH = "pyyaml";

    protected Object load(String data) {
        Yaml yaml = new Yaml();
        return yaml.load(data);
    }

    protected Object load(Constructor loader, String data) {
        Yaml yaml = new Yaml(loader);
        return yaml.load(data);
    }

    protected Iterable<Object> loadAll(InputStream data) {
        Yaml yaml = new Yaml();
        return yaml.loadAll(data);
    }

    protected Iterable<Object> loadAll(String data) {
        Yaml yaml = new Yaml();
        return yaml.loadAll(data);
    }

    protected Iterable<Object> loadAll(Constructor loader, String data) {
        Yaml yaml = new Yaml(loader);
        return yaml.loadAll(data);
    }

    protected String getResource(String theName) {
        String content;
        content = Util.getLocalResource(PATH + File.separator + theName);
        return content;
    }

    protected File[] getStreamsByExtension(String extention) {
        return getStreamsByExtension(extention, false);
    }

    protected File[] getStreamsByExtension(String extention, boolean onlyIfCanonicalPresent) {
        File file = new File("src/test/resources/pyyaml");
        assertTrue("Folder not found: " + file.getAbsolutePath(), file.exists());
        assertTrue(file.isDirectory());
        return file.listFiles(new PyFilenameFilter(extention, onlyIfCanonicalPresent));
    }

    protected File getFileByName(String name) {
        File file = new File("src/test/resources/pyyaml/" + name);
        assertTrue("Folder not found: " + file.getAbsolutePath(), file.exists());
        assertTrue(file.isFile());
        return file;
    }

    protected List<Event> canonicalParse(InputStream input2) throws IOException {
        StreamReader reader = new StreamReader(new UnicodeReader(input2));
        StringBuilder buffer = new StringBuilder();
        while (reader.peek() != '\0') {
            buffer.append(reader.peek());
            reader.forward();
        }
        CanonicalParser parser = new CanonicalParser(buffer.toString());
        List<Event> result = new ArrayList<Event>();
        while (parser.peekEvent() != null) {
            result.add(parser.getEvent());
        }
        input2.close();
        return result;
    }

    protected List<Event> parse(InputStream input) throws IOException {
        StreamReader reader = new StreamReader(new UnicodeReader(input));
        Parser parser = new ParserImpl(reader);
        List<Event> result = new ArrayList<Event>();
        while (parser.peekEvent() != null) {
            result.add(parser.getEvent());
        }
        input.close();
        return result;
    }

    private class PyFilenameFilter implements FilenameFilter {
        private String extension;
        private boolean onlyIfCanonicalPresent;

        public PyFilenameFilter(String extension, boolean onlyIfCanonicalPresent) {
            this.extension = extension;
            this.onlyIfCanonicalPresent = onlyIfCanonicalPresent;
        }

        public boolean accept(File dir, String name) {
            int position = name.lastIndexOf('.');
            String canonicalFileName = name.substring(0, position) + ".canonical";
            File canonicalFile = new File(dir, canonicalFileName);
            if (onlyIfCanonicalPresent && !canonicalFile.exists()) {
                return false;
            } else {
                return name.endsWith(extension);
            }
        }
    }
}
