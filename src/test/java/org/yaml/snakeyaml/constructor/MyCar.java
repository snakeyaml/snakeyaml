/*
 * See LICENSE file in distribution for copyright and licensing information.
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
