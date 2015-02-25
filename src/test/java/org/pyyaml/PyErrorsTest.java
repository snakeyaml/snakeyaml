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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.emitter.EventConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;

/**
 * @see imported from PyYAML
 */
public class PyErrorsTest extends PyImportTest {
    private boolean skip(String filename) {
        List<String> failures = new ArrayList<String>();
        // in python list cannot be a key in a dictionary.
        failures.add("unacceptable-key.loader-error");
        for (String name : failures) {
            if (name.equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public void testLoaderErrors() throws FileNotFoundException {
        File[] files = getStreamsByExtension(".loader-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            if (skip(files[i].getName())) {
                continue;
            }
            try {
                InputStream input = new FileInputStream(files[i]);
                for (Object document : loadAll(input)) {
                    assertNotNull("File " + files[i], document);
                }
                input.close();
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // System.err.println("Loading must fail for " +
                // files[i].getAbsolutePath());
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    public void testLoaderStringErrors() throws FileNotFoundException {
        File[] files = getStreamsByExtension(".loader-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            if (skip(files[i].getName())) {
                continue;
            }
            try {
                String content = getResource(files[i].getName());
                for (Object document : loadAll(content.trim())) {
                    assertNotNull(document);
                }
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // System.err.println("Loading must fail for " +
                // files[i].getAbsolutePath());
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    public void testLoaderSingleErrors() throws FileNotFoundException {
        File[] files = getStreamsByExtension(".single-loader-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            try {
                String content = getResource(files[i].getName());
                load(content.trim());
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // multiple documents must not be accepted
                System.err.println("Loading must fail for " + files[i].getAbsolutePath());
            } catch (YAMLException e) {
                assertTrue(true);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void testEmitterErrors() {
        File[] files = getStreamsByExtension(".emitter-error");
        assertTrue("No test files found.", files.length > 0);
        for (int i = 0; i < files.length; i++) {
            String content = getResource(files[i].getName());
            List<Event> document = (List<Event>) load(new EventConstructor(), content.trim());
            Writer writer = new StringWriter();
            Emitter emitter = new Emitter(writer, new DumperOptions());
            try {
                for (Event event : document) {
                    emitter.emit(event);
                }
                fail("Loading must fail for " + files[i].getAbsolutePath());
                // System.err.println("Loading must fail for " +
                // files[i].getAbsolutePath());
            } catch (Exception e) {
                assertTrue(true);
            }
        }
    }

    // testDumperErrors() is implemented in SerializerTest.java
}
