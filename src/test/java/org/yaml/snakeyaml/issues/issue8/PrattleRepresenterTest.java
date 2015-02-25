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
package org.yaml.snakeyaml.issues.issue8;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=8
 */
public class PrattleRepresenterTest extends TestCase {
    public void test() {
        Yaml yaml = new Yaml();
        Person person = new Person("Alan", "Gutierrez", 9);
        String etalon = "!!org.yaml.snakeyaml.issues.issue8.Person {firstName: Alan, hatSize: 9, lastName: Gutierrez}\n";
        assertEquals(etalon, yaml.dump(person));
        assertEquals(etalon, yaml.dump(person));
    }

    public void test2beans() {
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
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
