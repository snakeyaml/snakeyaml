/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.representer.Representer;

public class ImplicitTagsTest extends TestCase {

    public void testDefaultRepresenter() throws IOException {
        CarWithWheel car1 = new CarWithWheel();
        car1.setPlate("12-XP-F4");
        Wheel wheel = new Wheel();
        wheel.setId(2);
        car1.setWheel(wheel);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 3);
        car1.setMap(map);
        car1.setPart(new Wheel(4));
        car1.setYear("2008");
        String carYaml1 = new Yaml().dump(car1);
        assertEquals(Util.getLocalResource("constructor/carwheel-without-tags.yaml"), carYaml1);
        CarWithWheel car2 = (CarWithWheel) new Yaml().load(carYaml1);
        String carYaml2 = new Yaml().dump(car2);
        assertEquals(carYaml1, carYaml2);
    }

    public void testNoRootTag() throws IOException {
        CarWithWheel car1 = new CarWithWheel();
        car1.setPlate("12-XP-F4");
        Wheel wheel = new Wheel();
        wheel.setId(2);
        car1.setWheel(wheel);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 3);
        car1.setMap(map);
        car1.setYear("2008");
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot(Tags.MAP);
        String carYaml1 = new Yaml(options).dump(car1);
        assertEquals(Util.getLocalResource("constructor/car-without-root-tag.yaml"), carYaml1);
        //
        Constructor contructor = new Constructor(CarWithWheel.class);
        Loader loader = new Loader(contructor);
        CarWithWheel car2 = (CarWithWheel) new Yaml(loader).load(carYaml1);
        String carYaml2 = new Yaml(options).dump(car2);
        assertEquals(carYaml1, carYaml2);
    }

    @SuppressWarnings("unchecked")
    public void testRootMap() throws IOException {
        Map<Object, Object> car1 = new HashMap<Object, Object>();
        car1.put("plate", "12-XP-F4");
        Wheel wheel = new Wheel();
        wheel.setId(2);
        car1.put("wheel", wheel);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 3);
        car1.put("map", map);
        String carYaml1 = new Yaml().dump(car1);
        assertEquals(Util.getLocalResource("constructor/carwheel-root-map.yaml"), carYaml1);
        Map<Object, Object> car2 = (Map<Object, Object>) new Yaml().load(carYaml1);
        assertEquals(car1, car2);
        assertEquals(carYaml1, new Yaml().dump(car2));
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
        //
        String carYaml1 = new Yaml().dump(car);
        assertTrue(carYaml1.startsWith("!!org.yaml.snakeyaml.constructor.Car"));
        //
        Representer representer = new Representer();
        representer.addClassTag(Car.class, "!car");
        Dumper dumper = new Dumper(representer, new DumperOptions());
        yaml = new Yaml(dumper);
        String carYaml2 = yaml.dump(car);
        assertEquals(Util.getLocalResource("constructor/car-without-tags.yaml"), carYaml2);
    }

    public static class CarWithWheel {
        private String plate;
        private String year;
        private Wheel wheel;
        private Object part;
        private Map<String, Integer> map;

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }

        public Wheel getWheel() {
            return wheel;
        }

        public void setWheel(Wheel wheel) {
            this.wheel = wheel;
        }

        public Map<String, Integer> getMap() {
            return map;
        }

        public void setMap(Map<String, Integer> map) {
            this.map = map;
        }

        public Object getPart() {
            return part;
        }

        public void setPart(Object part) {
            this.part = part;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
