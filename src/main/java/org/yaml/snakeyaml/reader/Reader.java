/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.reader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Reader: determines the data encoding and converts it to unicode, checks if
 * characters are in allowed range, adds '\0' to the end.
 * 
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Reader {
    // NON_PRINTABLE changed from PyYAML: \uFFFD excluded because Java returns
    // it in case of data corruption
    final static Pattern NON_PRINTABLE = Pattern
            .compile("[^\t\n\r\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFC]");
    private final static String LINEBR = "\n\u0085\u2028\u2029";

    private String name;
    private final java.io.Reader stream;
    private int pointer = 0;
    private boolean eof = true;
    private final StringBuffer buffer;
    private int index = 0;
    private int line = 0;
    private int column = 0;

    public Reader(String stream) {
        this.name = "<string>";
        this.buffer = new StringBuffer();
        checkPrintable(stream);
        this.buffer.append(stream);
        this.stream = null;
        this.eof = true;
    }

    public Reader(java.io.Reader reader) {
        this.name = "<reader>";
        this.buffer = new StringBuffer();
        this.stream = reader;
        this.eof = false;
    }

    void checkPrintable(CharSequence data) {
        Matcher em = NON_PRINTABLE.matcher(data);
        if (em.find()) {
            int position = this.index + this.buffer.length() - this.pointer + em.start();
            throw new ReaderException(name, position, em.group().charAt(0),
                    " special characters are not allowed");
        }
    }

    public Mark getMark() {
        if (this.stream == null) {
            return new Mark(name, this.index, this.line, this.column, this.buffer.toString(),
                    this.pointer);
        } else {
            return new Mark(name, this.index, this.line, this.column, null, 0);
        }
    }

    public void forward() {
        forward(1);
    }

    /**
     * read the next length characters and move the pointer.
     * 
     * @param length
     */
    public void forward(int length) {
        if (this.pointer + length + 1 >= this.buffer.length()) {
            update(length + 1);
        }
        char ch = 0;
        for (int i = 0; i < length; i++) {
            ch = this.buffer.charAt(this.pointer);
            this.pointer++;
            this.index++;
            if (LINEBR.indexOf(ch) != -1
                    || (ch == '\r' && this.buffer.charAt(this.pointer) != '\n')) {
                this.line++;
                this.column = 0;
            } else if (ch != '\uFEFF') {
                this.column++;
            }
        }
    }

    public char peek() {
        return peek(0);
    }

    /**
     * Peek the next index-th character
     * 
     * @param index
     * @return
     */
    public char peek(int index) {
        if (this.pointer + index + 1 > this.buffer.length()) {
            update(index + 1);
        }
        return this.buffer.charAt(this.pointer + index);
    }

    /**
     * peek the next length characters
     * 
     * @param length
     * @return
     */
    public String prefix(int length) {
        if (this.pointer + length >= this.buffer.length()) {
            update(length);
        }
        if (this.pointer + length > this.buffer.length()) {
            return this.buffer.substring(this.pointer, this.buffer.length());
        } else {
            return this.buffer.substring(this.pointer, this.pointer + length);
        }
    }

    private void update(int length) {
        this.buffer.delete(0, this.pointer);
        this.pointer = 0;
        while (this.buffer.length() < length) {
            String rawData = "";
            if (!this.eof) {
                char[] data = new char[1024];
                int converted = -2;
                try {
                    converted = this.stream.read(data);
                } catch (IOException ioe) {
                    throw new YAMLException(ioe);
                }
                if (converted == -1) {
                    this.eof = true;
                } else {
                    rawData = new String(data, 0, converted);
                }
            }
            checkPrintable(rawData);
            this.buffer.append(rawData);
            if (this.eof) {
                this.buffer.append('\0');
                break;
            }
        }
    }

    public int getColumn() {
        return column;
    }

    public Charset getEncoding() {
        return Charset.forName(((UnicodeReader) this.stream).getEncoding());
    }

    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }
}
