package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public class WhitespaceToken extends Token {
    public WhitespaceToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override
    public ID getTokenId() {
        return ID.Whitespace;
    }
}
