/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

/**
 * Two constructors with 1 argument. These immutable objects are not supported.
 */
public class Code2 {
    private final Integer code;

    public Code2(Integer name) {
        this.code = name;
    }

    public Code2(String name) {
        this.code = Integer.parseInt(name);
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Code2) {
            Code2 code = (Code2) obj;
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
        return "<Code2 code=" + code + ">";
    }
}
