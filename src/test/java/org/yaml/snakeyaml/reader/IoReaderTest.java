/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.reader;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class IoReaderTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testCheckPrintable() throws IOException {
        Yaml yaml = new Yaml();
        java.io.Reader reader = null;
        reader = new FileReader("src/test/resources/specification/example2_1.yaml");
        List<String> list = (List<String>) yaml.load(reader);
        reader.close();
        assertEquals(3, list.size());
    }
}
