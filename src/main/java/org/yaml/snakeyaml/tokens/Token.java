/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public abstract class Token {
    private final Mark startMark;
    private final Mark endMark;

    public Token(Mark startMark, Mark endMark) {
        assert startMark != null;
        assert endMark != null;
        this.startMark = startMark;
        this.endMark = endMark;
    }

    public String toString() {
        return "<" + this.getClass().getName() + "(" + getArguments() + ")>";
    }

    public Mark getStartMark() {
        return startMark;
    }

    public Mark getEndMark() {
        return endMark;
    }

    /**
     * @see __repr__ for Token in PyYAML
     */
    protected String getArguments() {
        return "";
    }

    /**
     * For error reporting.
     * 
     * @see class variable 'id' in PyYAML
     */
    public abstract String getTokenId();

    /*
     * for tests only
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            return toString().equals(obj.toString());
        } else {
            return false;
        }
    }
}
