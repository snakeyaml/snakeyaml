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
package org.yaml.snakeyaml.immutable.primitives;

public class BunchOfPrimitives {
    private int primitiveInt;
    private double primitiveDouble;
    public boolean primitiveBoolean;

    public BunchOfPrimitives(int primitiveInt, double primitiveDouble, boolean primitiveBoolean) {
        this.primitiveInt = primitiveInt;
        this.primitiveDouble = primitiveDouble;
        this.primitiveBoolean = primitiveBoolean;
    }

    /**
     * The number of parameters is the same but the type is different
     */
    public BunchOfPrimitives(int i1, int i2, int i3) {
        this.primitiveInt = i1;
    }

    public BunchOfPrimitives(long i1, double i2, boolean i3) {
        this((int) i1, i2, i3);
    }

    public int getPrimitiveInt() {
        return primitiveInt;
    }

    public double getPrimitiveDouble() {
        return primitiveDouble;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BunchOfPrimitives) {
            BunchOfPrimitives bunch = (BunchOfPrimitives) obj;
            return primitiveInt == bunch.primitiveInt;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return primitiveInt;
    }

    @Override
    public String toString() {
        return "BunchOfPrimitives " + primitiveInt;
    }
}
