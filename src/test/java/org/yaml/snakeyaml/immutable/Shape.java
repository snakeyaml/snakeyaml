/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
