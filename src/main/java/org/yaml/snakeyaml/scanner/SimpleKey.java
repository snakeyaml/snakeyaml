/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.error.Mark;

/**
 * Simple keys treatment.
 * 
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
final class SimpleKey {
    private int tokenNumber;
    private boolean required;
    private int index;
    private int line;
    private int column;
    private Mark mark;

    public SimpleKey(int tokenNumber, boolean required, int index, int line, int column, Mark mark) {
        this.tokenNumber = tokenNumber;
        this.required = required;
        this.index = index;
        this.line = line;
        this.column = column;
        this.mark = mark;
    }

    public int getTokenNumber() {
        return this.tokenNumber;
    }

    public int getColumn() {
        return this.column;
    }

    public Mark getMark() {
        return mark;
    }

    public int getIndex() {
        return index;
    }

    public int getLine() {
        return line;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "SimpleKey - tokenNumber=" + tokenNumber + " required=" + required + " index="
                + index + " line=" + line + " column=" + column;
    }
}
