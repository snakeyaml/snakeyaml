/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.yaml.snakeyaml.reader.Reader;
import org.yaml.snakeyaml.reader.ReaderException;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 * @see imported from PyYAML
 */
public class PyReaderTest extends PyImportTest {

    public void testReaderUnicodeErrors() throws IOException {
        File[] inputs = getStreamsByExtension(".stream-error");
        for (int i = 0; i < inputs.length; i++) {
            Reader stream = new Reader(new UnicodeReader(new FileInputStream(inputs[i])));
            try {
                while (stream.peek() != '\u0000') {
                    stream.forward();
                }
                fail("Invalid stream must not be accepted: " + inputs[i].getAbsolutePath()
                        + "; encoding=" + stream.getEncoding());
            } catch (ReaderException e) {
                System.out.println(e.toString());
                assertTrue(true);
            }
        }
    }

}
