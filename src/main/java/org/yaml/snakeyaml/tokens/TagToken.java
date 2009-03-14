/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class TagToken extends Token {
    private final String[] value;

    public TagToken(String[] value, Mark startMark, Mark endMark) {
        super(startMark, endMark);
        if (value.length != 2) {
            throw new YAMLException("Two strings must be provided instead of "
                    + String.valueOf(value.length));
        }
        this.value = value;
    }

    public String[] getValue() {
        return this.value;
    }

    @Override
    protected String getArguments() {
        return "value=[" + value[0] + ", " + value[1] + "]";
    }

    @Override
    public String getTokenId() {
        return "<tag>";
    }
}
