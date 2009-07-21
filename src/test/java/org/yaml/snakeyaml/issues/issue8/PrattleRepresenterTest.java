/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.issues.issue8;

import java.io.IOException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=8
 */
public class PrattleRepresenterTest extends TestCase {
    public void test() throws IOException {
        Yaml yaml = new Yaml();
        Person person = new Person("Alan", "Gutierrez", 9);
        String etalon = "!!org.yaml.snakeyaml.issues.issue8.Person {firstName: Alan, hatSize: 9, lastName: Gutierrez}\n";
        assertEquals(etalon, yaml.dump(person));
        assertEquals(etalon, yaml.dump(person));
    }

    public void test2beans() throws IOException {
        Yaml yaml = new Yaml();
        Person person = new Person("Alan", "Gutierrez", 9);
        String etalon = "!!org.yaml.snakeyaml.issues.issue8.Person {firstName: Alan, hatSize: 9, lastName: Gutierrez}\n";
        assertEquals(etalon, yaml.dump(person));
        Horse horse = new Horse("Tom", person);
        String etalon2 = "!!org.yaml.snakeyaml.issues.issue8.PrattleRepresenterTest$Horse\nname: Tom\nowner: {firstName: Alan, hatSize: 9, lastName: Gutierrez}\n";
        assertEquals(etalon2, yaml.dump(horse));
    }

    public static class Horse {
        private String name;
        private Person owner;

        public Horse(String name, Person owner) {
            super();
            this.name = name;
            this.owner = owner;
        }

        public String getName() {
            return name;
        }

        public Person getOwner() {
            return owner;
        }

    }
}
