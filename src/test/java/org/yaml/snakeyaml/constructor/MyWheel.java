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
package org.yaml.snakeyaml.constructor;

public class MyWheel implements Comparable<MyWheel> {
    private int id;
    private String brand;

    public MyWheel() {
        brand = "Pirelli";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Wheel id=" + id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MyWheel) {
            MyWheel wheel = (MyWheel) obj;
            return id == wheel.getId();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int compareTo(MyWheel arg0) {
        return new Integer(id).compareTo(new Integer(arg0.id));
    }
}
