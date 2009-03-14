/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.reader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;

import junit.framework.TestCase;

public class ReaderBomTest extends TestCase {

    public void testReader() throws IOException {
        java.io.Reader input = new StringReader("test");
        Reader reader = new Reader(input);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
    }

    public void testNoBom() throws IOException {
        byte[] data = "test".getBytes("UTF-8");
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        java.io.Reader r = new UnicodeReader(input);
        Reader reader = new Reader(r);
        assertEquals('t', reader.peek());
        assertEquals(Charset.forName("UTF-8"), reader.getEncoding());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
        r.close();
    }

    public void testUtf8Bom() throws IOException {
        File file = new File("src/test/resources/reader/utf-8.txt");
        assertTrue("Test file not found: " + file.getAbsolutePath(), file.exists());
        InputStream input = new FileInputStream(file);
        Reader reader = new Reader(new UnicodeReader(input));
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
        assertEquals(Charset.forName("UTF-8"), reader.getEncoding());
    }

    public void testUnicodeLeBom() throws IOException {
        File file = new File("src/test/resources/reader/unicode-16le.txt");
        assertTrue("Test file not found: " + file.getAbsolutePath(), file.exists());
        InputStream input = new FileInputStream(file);
        Reader reader = new Reader(new UnicodeReader(input));
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
        assertEquals(Charset.forName("UTF-16LE"), reader.getEncoding());
    }

    public void testUnicodeBeBom() throws IOException {
        File file = new File("src/test/resources/reader/unicode-16be.txt");
        assertTrue("Test file not found: " + file.getAbsolutePath(), file.exists());
        InputStream input = new FileInputStream(file);
        Reader reader = new Reader(new UnicodeReader(input));
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('e', reader.peek());
        reader.forward(1);
        assertEquals('s', reader.peek());
        reader.forward(1);
        assertEquals('t', reader.peek());
        reader.forward(1);
        assertEquals('\u0000', reader.peek());
        assertEquals(Charset.forName("UTF-16BE"), reader.getEncoding());
    }

}
