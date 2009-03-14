/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class ArrayTagsTest extends TestCase {

    public void testDefaultRepresenter() throws IOException {
        CarWithArray car = new CarWithArray();
        car.setPlate("12-XP-F4");
        Wheel[] wheels = new Wheel[5];
        for (int i = 1; i < 6; i++) {
            Wheel wheel = new Wheel();
            wheel.setId(i);
            wheels[i - 1] = wheel;
        }
        car.setWheels(wheels);
        assertEquals(Util.getLocalResource("constructor/cararray-with-tags.yaml"), new Yaml()
                .dump(car));
    }

    public void testLoadClassTag() throws IOException {
        Constructor constructor = new Constructor();
        constructor.addTypeDescription(new TypeDescription(Car.class, "!car"));
        Loader loader = new Loader(constructor);
        Yaml yaml = new Yaml(loader);
        Car car = (Car) yaml.load(Util.getLocalResource("constructor/car-without-tags.yaml"));
        assertEquals("12-XP-F4", car.getPlate());
        List<Wheel> wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.size());
    }

    public void testNullDescription() throws IOException {
        Constructor constructor = new Constructor();
        try {
            constructor.addTypeDescription(null);
            fail("Description is required.");
        } catch (Exception e) {
            assertEquals("TypeDescription is required.", e.getMessage());
        }
    }

    public void testLoadClassNoRoot() throws IOException {
        Constructor constructor = new Constructor();
        TypeDescription carDescription = new TypeDescription(CarWithArray.class);
        carDescription.setRoot(true);
        constructor.addTypeDescription(carDescription);
        Loader loader = new Loader(constructor);
        Yaml yaml = new Yaml(loader);
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
