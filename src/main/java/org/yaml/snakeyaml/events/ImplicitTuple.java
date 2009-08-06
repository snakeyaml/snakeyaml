/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.events;

/**
 * The implicit flag of a scalar event is a pair of boolean values that indicate
 * if the tag may be omitted when the scalar is emitted in a plain and non-plain
 * style correspondingly.
 * 
 * @see http://pyyaml.org/wiki/PyYAMLDocumentation#Events
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class ImplicitTuple {
    private final boolean plain;
    private final boolean nonPlain;

    public ImplicitTuple(boolean plain, boolean nonplain) {
        this.plain = plain;
        this.nonPlain = nonplain;
    }

    /**
     * @return true when tag may be omitted when the scalar is emitted in a
     *         plain style.
     */
    public boolean isFirst() {
        return plain;
    }

    /**
     * @return true when tag may be omitted when the scalar is emitted in a
     *         non-plain style.
     */
    public boolean isSecond() {
        return nonPlain;
    }

    public boolean bothFalse() {
        return !plain && !nonPlain;
    }

    @Override
    public String toString() {
        return "implicit=[" + plain + ", " + nonPlain + "]";
    }
}
