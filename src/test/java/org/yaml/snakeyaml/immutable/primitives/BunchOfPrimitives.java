/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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
    private long primitiveLong;
    private float primitiveFloat;
    private double primitiveDouble;
    private boolean primitiveBoolean;
    public char primitiveChar;
    public short primitiveShort;
    public byte primitiveByte;

    public BunchOfPrimitives(int primitiveInt, long primitiveLong, float primitiveFloat,
            double primitiveDouble, boolean primitiveBoolean, char primitiveChar,
            short primitiveShort, byte primitiveByte) {
        this.primitiveInt = primitiveInt;
        this.primitiveLong = primitiveLong;
        this.primitiveFloat = primitiveFloat;
        this.primitiveDouble = primitiveDouble;
        this.primitiveBoolean = primitiveBoolean;
        this.primitiveChar = primitiveChar;
        this.primitiveShort = primitiveShort;
        this.primitiveByte = primitiveByte;
    }

    /**
     * The number of parameters is the same but the type is different
     */
    public BunchOfPrimitives(int i1, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.primitiveInt = i1;
    }

    public int getPrimitiveInt() {
        return primitiveInt;
    }

    public long getPrimitiveLong() {
        return primitiveLong;
    }

    public float getPrimitiveFloat() {
        return primitiveFloat;
    }

    public double getPrimitiveDouble() {
        return primitiveDouble;
    }

    public boolean isPrimitiveBoolean() {
        return primitiveBoolean;
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
