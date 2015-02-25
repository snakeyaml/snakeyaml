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

import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class ArrayTagsTest extends TestCase {

    public void testDefaultRepresenter() {
        CarWithArray car = new CarWithArray();
        car.setPlate("12-XP-F4");
        Wheel[] wheels = new Wheel[5];
        for (int i = 1; i < 6; i++) {
            Wheel wheel = new Wheel();
            wheel.setId(i);
            wheels[i - 1] = wheel;
        }
        car.setWheels(wheels);
        assertEquals(Util.getLocalResource("constructor/cararray-with-tags-flow-auto.yaml"),
                new Yaml().dump(car));
    }

    public void testFlowBlock() {
        CarWithArray car = new CarWithArray();
        car.setPlate("12-XP-F4");
        Wheel[] wheels = new Wheel[5];
        for (int i = 1; i < 6; i++) {
            Wheel wheel = new Wheel();
            wheel.setId(i);
            wheels[i - 1] = wheel;
        }
        car.setWheels(wheels);
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        assertEquals(Util.getLocalResource("constructor/cararray-with-tags.yaml"), yaml.dump(car));
    }

    public void testLoadClassTag() {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(Car.class, "!car"));
        Yaml yaml = new Yaml(constructor);
        Car car = (Car) yaml.load(Util.getLocalResource("constructor/car-without-tags.yaml"));
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
        Constructor constructor = new Constructor(new TypeDescription(CarWithArray.class));
        Yaml yaml = new Yaml(constructor);
        CarWithArray car = (CarWithArray) yaml.load(Util
                .getLocalResource("constructor/car-no-root-class.yaml"));
        assertEquals("12-XP-F4", car.getPlate());
        Wheel[] wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.length);
    }

    public static class CarWithArray {
        private String plate;
        private Wheel[] wheels;

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public Wheel[] getWheels() {
            return wheels;
        }

        public void setWheels(Wheel[] wheels) {
            this.wheels = wheels;
        }
    }
}
