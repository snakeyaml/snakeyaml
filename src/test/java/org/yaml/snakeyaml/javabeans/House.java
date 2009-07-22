/*
 * See LICENSE file in distribution for copyright and licensing information.
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
