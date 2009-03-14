/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import java.util.List;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class DirectiveToken extends Token {
    private final String name;
    private final List<?> value;

    public DirectiveToken(String name, List<?> value, Mark startMark, Mark endMark) {
        super(startMark, endMark);
        this.name = name;
        if (value != null && value.size() != 2) {
            throw new YAMLException("Two strings must be provided instead of "
                    + String.valueOf(value.size()));
        }
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public List<?> getValue() {
        return this.value;
    }

    @Override
    protected String getArguments() {
        if (value != null) {
            return "name=" + name + ", value=[" + value.get(0) + ", " + value.get(1) + "]";
        } else {
            return "name=" + name;
        }
    }

    @Override
    public String getTokenId() {
        return "<directive>";
    }
}
