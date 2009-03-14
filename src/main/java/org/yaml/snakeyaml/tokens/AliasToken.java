/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class AliasToken extends Token {
    private final String value;

    public AliasToken(String value, Mark startMark, Mark endMark) {
        super(startMark, endMark);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    protected String getArguments() {
        return "value=" + value;
    }

    @Override
    public String getTokenId() {
        return "<alias>";
    }
}
