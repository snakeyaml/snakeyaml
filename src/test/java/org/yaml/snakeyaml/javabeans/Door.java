/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.javabeans;

public class Door {
    private String id;
    private int height;

    public Door(String id, int height) {
        this.id = id;
        this.height = height;
    }

    public Door() {
        this.height = 3;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Door) {
            Door door = (Door) obj;
            return id.equals(door.id);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Door id=" + id;
    }

}
