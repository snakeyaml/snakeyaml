/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

public class Point3d {
    private final double z;
    private final Point point;

    public Point3d(Point point, Double z) {
        this.point = point;
        this.z = z;
    }

    public double getZ() {
        return z;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public String toString() {
        return "<Point3d point=" + point.toString() + " z=" + String.valueOf(z) + ">";
    }
}
