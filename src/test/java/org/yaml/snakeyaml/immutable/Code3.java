/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

/**
 * No constructors with 1 argument. These immutable objects are not supported.
 */
public class Code3 {
    private final String name;
    private final Integer code;

    public Code3(String name, Integer code) {
        this.code = code;
        this.name = name;
    }

    public String getData() {
        return name + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Code3) {
            Code3 code = (Code3) obj;
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
        return "<Code3 data=" + getData() + ">";
    }
}
