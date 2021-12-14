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
package examples;

import junit.framework.TestCase;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class LoadExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testLoad() {
        Yaml yaml = new Yaml();
        String document = "\n- Hesperiidae\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
        List<String> list = (List<String>) yaml.load(document);
        assertEquals("[Hesperiidae, Papilionidae, Apatelodidae, Epiplemidae]", list.toString());
    }

    public void testLoadFromString() {
        Yaml yaml = new Yaml();
        String document = "hello: 25";
        @SuppressWarnings("unchecked")
        Map<String, Integer> map = (Map<String, Integer>) yaml.load(document);
        assertEquals("{hello=25}", map.toString());
        assertEquals(Integer.valueOf(25), map.get("hello"));
    }

    public void testLoadFromStream() throws IOException {
        InputStream input = new FileInputStream(new File("src/test/resources/reader/utf-8.txt"));
        Yaml yaml = new Yaml();
        Object data = yaml.load(input);
        assertEquals("test", data);
        //
        data = yaml.load(new ByteArrayInputStream("test2".getBytes("UTF-8")));
        assertEquals("test2", data);
        input.close();
    }

    public void testLoadManyDocuments() throws IOException {
        InputStream input = new FileInputStream(
                new File("src/test/resources/specification/example2_28.yaml"));
        Yaml yaml = new Yaml();
        int counter = 0;
        for (Object data : yaml.loadAll(input)) {
            assertNotNull(data);
            assertTrue(data.toString().length() > 1);
            counter++;
        }
        assertEquals(3, counter);
        input.close();
    }

    public void testLoadManyDocumentsWithIterator() throws IOException {
        InputStream input = new FileInputStream(
                new File("src/test/resources/specification/example2_28.yaml"));
        Yaml yaml = new Yaml();
        int counter = 0;
        Iterator<Object> iter = yaml.loadAll(input).iterator();
        while (iter.hasNext()) {
            Object data = iter.next();
            assertNotNull(data);
            assertTrue(data.toString().length() > 1);
            counter++;
        }
        assertEquals(3, counter);
        input.close();
    }

    public void testLoadManyDocumentsWithIterator2() throws IOException {
        InputStream input = new FileInputStream(
                new File("src/test/resources/specification/example2_28.yaml"));
        Yaml yaml = new Yaml();
        Iterator<Object> iter = yaml.loadAll(input).iterator();
        Object data = iter.next();
        assertNotNull(data);
        data = iter.next();
        assertNotNull(data);
        data = iter.next();
        assertNotNull(data);
        try {
            iter.next();
            fail("Expect NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertEquals("No document is available.", e.getMessage());
        }
        input.close();
    }
}
