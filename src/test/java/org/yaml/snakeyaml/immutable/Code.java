/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

public class Code {
    private final Integer code;

    public Code(Integer name) {
        this.code = name;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Code) {
            Code code = (Code) obj;
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
        return "Code code=" + code;
    }
}
