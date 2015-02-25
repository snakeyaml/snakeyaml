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
package org.yaml.snakeyaml.generics;

import java.util.Map;

public class ObjectValues {

    private Object object;
    private Map<String, Map<Integer, Object>> values;
    private String[] possible;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setValues(Map<String, Map<Integer, Object>> values) {
        this.values = values;
    }

    public Map<String, Map<Integer, Object>> getValues() {
        return values;
    }

    public void setPossible(String[] possible) {
        this.possible = possible;
    }

    public String[] getPossible() {
        return possible;
    }
}
