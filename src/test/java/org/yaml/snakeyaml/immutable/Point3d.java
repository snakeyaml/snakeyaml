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
