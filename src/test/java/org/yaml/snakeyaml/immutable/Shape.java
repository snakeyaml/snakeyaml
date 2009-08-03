/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

public class Shape {
    private Color color;
    private Point point;
    private Point3d point3d;
    private Integer id;

    public Point3d getPoint3d() {
        return point3d;
    }

    public void setPoint3d(Point3d point3d) {
        this.point3d = point3d;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public Point getPoint() {
        return point;
    }

    public Integer getId() {
        return id;
    }
}
