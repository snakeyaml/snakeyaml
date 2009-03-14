/**
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.emitter;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
final class ScalarAnalysis {
    public String scalar;
    public boolean empty;
    public boolean multiline;
    public boolean allowFlowPlain;
    public boolean allowBlockPlain;
    public boolean allowSingleQuoted;
    public boolean allowDoubleQuoted;
    public boolean allowBlock;

    public ScalarAnalysis(String scalar, boolean empty, boolean multiline, boolean allowFlowPlain,
            boolean allowBlockPlain, boolean allowSingleQuoted, boolean allowDoubleQuoted,
            boolean allowBlock) {
        this.scalar = scalar;
        this.empty = empty;
        this.multiline = multiline;
        this.allowFlowPlain = allowFlowPlain;
        this.allowBlockPlain = allowBlockPlain;
        this.allowSingleQuoted = allowSingleQuoted;
        this.allowDoubleQuoted = allowDoubleQuoted;
        this.allowBlock = allowBlock;
    }
}