/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue8;

import java.io.Serializable;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=8
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private int hatSize;

    public Person() {
    }

    public Person(String firstName, String lastName, int hatSize) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.hatSize = hatSize;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getHatSize() {
        return hatSize;
    }

    public void setHatSize(int hatSize) {
        this.hatSize = hatSize;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Person) {
            Person person = (Person) object;
            return firstName.equals(person.firstName) && lastName.equals(person.lastName)
                    && hatSize == person.hatSize;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
