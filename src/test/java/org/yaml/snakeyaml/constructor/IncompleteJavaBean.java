/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

public class IncompleteJavaBean {
    private int number;
    private String name = "No name";
    private float amount;

    public String getName() {
        return name;
    }

    public void setNumber(int number) {
        this.number = number;
        amount += number;
    }

    public int obtainNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "<IncompleteJavaBean name=" + name + ">";
    }
}