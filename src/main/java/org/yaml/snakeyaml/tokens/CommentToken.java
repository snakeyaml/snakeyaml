package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public class CommentToken extends Token {
    public CommentToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override
    public ID getTokenId() {
        return ID.Comment;
    }
}
