/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

public class MyWheel {
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
}
