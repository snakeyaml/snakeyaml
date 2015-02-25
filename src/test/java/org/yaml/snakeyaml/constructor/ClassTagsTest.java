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
package org.yaml.snakeyaml.constructor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class ClassTagsTest extends TestCase {

    public void testDefaultRepresenter() {
        Car car = new Car();
        car.setPlate("12-XP-F4");
        List<Wheel> wheels = new ArrayList<Wheel>();
        for (int i = 1; i < 6; i++) {
            Wheel wheel = new Wheel();
            wheel.setId(i);
            wheels.add(wheel);
        }
        car.setWheels(wheels);
        assertEquals(Util.getLocalResource("constructor/car-with-tags.yaml"), new Yaml().dump(car));
    }

    public void testDumpClassTag() {
        Car car = new Car();
        car.setPlate("12-XP-F4");
        List<Wheel> wheels = new ArrayList<Wheel>();
        for (int i = 1; i < 6; i++) {
            Wheel wheel = new Wheel();
            wheel.setId(i);
            wheels.add(wheel);
        }
        car.setWheels(wheels);
        Representer representer = new Representer();
        representer.addClassTag(Car.class, new Tag("!car"));
        representer.addClassTag(Wheel.class, Tag.MAP);
        Yaml yaml = new Yaml(representer);
        String output = yaml.dump(car);
        assertEquals(Util.getLocalResource("constructor/car-without-tags.yaml"), output);
    }

    public void testLoadUnknounClassTag() {
        try {
            Yaml yaml = new Yaml();
            yaml.load(Util.getLocalResource("constructor/car-without-tags.yaml"));
            fail("Must fail because of unknown tag: !car");
        } catch (YAMLException e) {
            assertTrue(e.getMessage().contains("Invalid tag: !car"));
        }

    }

    public void testLoadClassTag() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(Car.class, "!car"));
        Yaml yaml = new Yaml(constructor);
        String source = Util.getLocalResource("constructor/car-without-tags.yaml");
        Car car = (Car) yaml.load(source);
        assertEquals("12-XP-F4", car.getPlate());
        List<Wheel> wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.size());
    }

    public void testNullDescription() {
        Constructor constructor = new Constructor();
        try {
            constructor.addTypeDescription(null);
            fail("Description is required.");
        } catch (Exception e) {
            assertEquals("TypeDescription is required.", e.getMessage());
        }
    }

    public void testLoadClassNoRoot() {
        Constructor constructor = new Constructor(new TypeDescription(Car.class));
        Yaml yaml = new Yaml(constructor);
        Car car = (Car) yaml.load(Util.getLocalResource("constructor/car-no-root-class.yaml"));
        assertEquals("12-XP-F4", car.getPlate());
        List<Wheel> wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.size());
    }
}
