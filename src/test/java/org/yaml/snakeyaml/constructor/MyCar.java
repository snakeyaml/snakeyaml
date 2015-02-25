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

import java.util.Date;
import java.util.Map;

public class MyCar {
    private String plate;
    private Map<MyWheel, Date> wheels;
    private Map<String, Integer> windows;

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Map<MyWheel, Date> getWheels() {
        return wheels;
    }

    public void setWheels(Map<MyWheel, Date> wheels) {
        this.wheels = wheels;
    }

    public Map<String, Integer> getWindows() {
        return windows;
    }

    public void setWindows(Map<String, Integer> windows) {
        this.windows = windows;
    }
}
