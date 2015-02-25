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
package org.yaml.snakeyaml.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

public class SerializerTest extends TestCase {
    private Serializer serializer;

    @Override
    protected void setUp() {
        DumperOptions config = new DumperOptions();
        StringWriter writer = new StringWriter();
        serializer = new Serializer(new Emitter(writer, config), new Resolver(), config, null);
    }

    public void testSerializerIsAlreadyOpened() throws IOException {
        serializer.open();
        try {
            serializer.open();
            fail();
        } catch (RuntimeException e) {
            assertEquals("serializer is already opened", e.getMessage());
        }
    }

    public void testSerializerIsClosed1() throws IOException {
        serializer.open();
        serializer.close();
        try {
            serializer.open();
            fail();
        } catch (RuntimeException e) {
            assertEquals("serializer is closed", e.getMessage());
        }
    }

    public void testSerializerIsClosed2() throws IOException {
        serializer.open();
        serializer.close();
        try {
            serializer.serialize(new ScalarNode(new Tag("!foo"), "bar", null, null, (char) 0));
            fail();
        } catch (RuntimeException e) {
            assertEquals("serializer is closed", e.getMessage());
        }
    }

    public void testSerializerIsClosed3() throws IOException {
        serializer.open();
        serializer.close();
        serializer.close();// no problem to close twice
    }

    public void testSerializerIsNotOpened1() throws IOException {
        try {
            serializer.close();
            fail();
        } catch (RuntimeException e) {
            assertEquals("serializer is not opened", e.getMessage());
        }
    }

    public void testSerializerIsNotOpened2() throws IOException {
        try {
            serializer.serialize(new ScalarNode(new Tag("!foo"), "bar", null, null, (char) 0));
            fail();
        } catch (RuntimeException e) {
            assertEquals("serializer is not opened", e.getMessage());
        }
    }

    public void testGenerateAnchor() {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(3);
        String anchor = format.format(3L);
        assertEquals("003", anchor);
    }
}
