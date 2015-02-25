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
package org.yaml.snakeyaml.issues.issue64;

import java.util.List;

public class MethodDesc {
    private String name;
    private List<Class<?>> argTypes;

    public MethodDesc() {
    }

    public MethodDesc(String name, List<Class<?>> argTypes) {
        this.name = name;
        this.argTypes = argTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Class<?>> getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(List<Class<?>> argTypes) {
        this.argTypes = argTypes;
    }
}
