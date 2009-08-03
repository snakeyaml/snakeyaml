/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

public class SuperColor {
    private final Color color;

    public SuperColor(Color name) {
        this.color = name;
    }

    public Color getName() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SuperColor) {
            SuperColor color = (SuperColor) obj;
            return color.equals(color.color);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }

    @Override
    public String toString() {
        return "SuperColor color=" + color;
    }
}
