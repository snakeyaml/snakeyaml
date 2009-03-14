/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.reader;

import org.yaml.snakeyaml.error.YAMLException;

public class ReaderException extends YAMLException {
    private static final long serialVersionUID = 8710781187529689083L;
    private String name;
    private char character;
    private int position;

    public ReaderException(String name, int position, char character, String message) {
        super(message);
        this.name = name;
        this.character = character;
        this.position = position;
    }

    @Override
    public String toString() {
        return "unacceptable character #" + Integer.toHexString((int) character).toUpperCase()
                + " " + getMessage() + "\nin \"" + name + "\", position " + position;
    }
}
