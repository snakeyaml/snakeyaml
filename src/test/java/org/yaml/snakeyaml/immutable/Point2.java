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
