/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml;

import java.util.LinkedHashMap;

public class EnumBeanGen<T extends Enum<T>> {
    private int id;
    private Enum<T> suit;
    private LinkedHashMap<T, Integer> map = new LinkedHashMap<T, Integer>();

    public LinkedHashMap<T, Integer> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<T, Integer> map) {
        this.map = map;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Enum<T> getSuit() {
        return suit;
    }

    public void setSuit(T suit) {
        this.suit = suit;
    }
}
