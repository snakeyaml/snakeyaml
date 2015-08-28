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
package org.yaml.snakeyaml.issues.issue311;

public enum BooleanEnum {

    TRUE(true), FALSE(false), UNKNOWN();

    private boolean boolValue;
    private boolean defined;

    BooleanEnum(boolean p) {
        boolValue = p;
        defined = true;
    }

    BooleanEnum() {
        boolValue = false;
        defined = false;
    }

    boolean getBoolValue() {
        if (!defined)
            throw new IllegalArgumentException("Undefined has no value");
        else
            return boolValue;
    }

    boolean isDefined() {
        return defined;
    }
}