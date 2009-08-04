/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

public class Point {
    private final double x;
    private final double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // TODO try primitive arguments
    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "<Point x=" + String.valueOf(x) + " y=" + String.valueOf(y) + ">";
    }
}
