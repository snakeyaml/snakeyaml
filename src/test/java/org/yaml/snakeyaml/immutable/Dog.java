/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

public class Dog implements Animal {
    private String name;

    public Dog(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void bark() {
        System.out.println("I am a " + name);
    }
}
