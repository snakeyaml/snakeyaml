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
package org.yaml.snakeyaml.javabeans;

import java.util.List;
import java.util.Map;

public class House {
    private String street;
    private int number;
    private List<Room> rooms;
    private FrontDoor frontDoor;
    private Map<String, String> reminders;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public FrontDoor getFrontDoor() {
        return frontDoor;
    }

    public void setFrontDoor(FrontDoor frontDoor) {
        this.frontDoor = frontDoor;
    }

    public Map<String, String> getReminders() {
        return reminders;
    }

    public void setReminders(Map<String, String> reminders) {
        this.reminders = reminders;
    }

}
