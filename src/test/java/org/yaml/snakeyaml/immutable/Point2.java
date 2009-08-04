/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

/**
 * Two public constructor with 2 argument are present
 */
public class Point2 {
    private final Integer x;
    private final Integer y;

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Point2(Double x, Double y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    public Point2(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "<Point2 x=" + String.valueOf(x) + " y=" + String.valueOf(y) + ">";
    }
}
