/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

public class Wheel {
    private int id;

    public Wheel(int id) {
        this.id = id;
    }

    public Wheel() {
        this(0);
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
        if (obj instanceof Wheel) {
            Wheel wheel = (Wheel) obj;
            return id == wheel.getId();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new Integer(id).hashCode();
    }
}
