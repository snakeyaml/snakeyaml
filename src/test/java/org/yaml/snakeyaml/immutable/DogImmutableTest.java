/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.immutable;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class DogImmutableTest extends TestCase {

    public void testDog() {
        Yaml yaml = new Yaml();
        Dog loaded = (Dog) yaml.load("!!org.yaml.snakeyaml.immutable.Dog Bulldog");
        assertEquals("Bulldog", loaded.getName());
    }

    public void testHouse() {
        Yaml yaml = new Yaml();
        HouseBean loaded = (HouseBean) yaml
                .load("!!org.yaml.snakeyaml.immutable.HouseBean\nanimal: !!org.yaml.snakeyaml.immutable.Dog Bulldog");
        assertEquals("Bulldog", loaded.getAnimal().getName());
    }
}
