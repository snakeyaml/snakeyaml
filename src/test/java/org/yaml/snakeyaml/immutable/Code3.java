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
 * No constructors with 1 argument. These immutable objects are not supported.
 */
public class Code3 {
    private final String name;
    private final Integer code;

    public Code3(String name, Integer code) {
        this.code = code;
        this.name = name;
    }

    public String getData() {
        return name + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Code3) {
            Code3 code = (Code3) obj;
            return code.equals(code.code);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "<Code3 data=" + getData() + ">";
    }
}
