/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public final class BlockEndToken extends Token {

    public BlockEndToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override
    public String getTokenId() {
        return "<block end>";
    }
}
