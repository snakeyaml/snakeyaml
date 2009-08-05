/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

/**
 * Two constructors with 1 argument. None of them has String as the argument
 * class.
 */
public class Code4 {
    private final Integer code;

    public Code4(Integer name) {
        this.code = name;
    }

    public Code4(Double name) {
        this.code = new Integer(name.intValue());
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Code4) {
            Code4 code = (Code4) obj;
            return code.equals(code.code);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "<Code4 code=" + code + ">";
    }
}
