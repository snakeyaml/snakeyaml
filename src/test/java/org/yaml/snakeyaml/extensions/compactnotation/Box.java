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
package org.yaml.snakeyaml.extensions.compactnotation;

public class Box {
    private String id;
    private String name;
    private Item top;
    private Item bottom;

    public Box(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Item getTop() {
        return top;
    }

    public void setTop(Item top) {
        this.top = top;
    }

    public Item getBottom() {
        return bottom;
    }

    public void setBottom(Item bottom) {
        this.bottom = bottom;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
