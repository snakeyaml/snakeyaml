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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class ImplicitTagsTest extends TestCase {

    public void testDefaultRepresenter() {
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

    public void testNoRootTag() {
        CarWithWheel car1 = new CarWithWheel();
        car1.setPlate("12-XP-F4");
        Wheel wheel = new Wheel();
        wheel.setId(2);
        car1.setWheel(wheel);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 3);
        car1.setMap(map);
        car1.setYear("2008");
        String carYaml1 = new Yaml().dumpAs(car1, Tag.MAP, FlowStyle.AUTO);
        assertEquals(Util.getLocalResource("constructor/car-without-root-tag.yaml"), carYaml1);
        //
        Constructor contructor = new Constructor(CarWithWheel.class);
        CarWithWheel car2 = (CarWithWheel) new Yaml(contructor).load(carYaml1);
        String carYaml2 = new Yaml().dumpAs(car2, Tag.MAP, FlowStyle.AUTO);
        assertEquals(carYaml1, carYaml2);
    }

    @SuppressWarnings("unchecked")
    public void testRootMap() {
        Map<Object, Object> car1 = new LinkedHashMap<Object, Object>();
        Wheel wheel = new Wheel();
        wheel.setId(2);
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("id", 3);

        car1.put("wheel", wheel);
        car1.put("map", map);
        car1.put("plate", "12-XP-F4");

        String carYaml1 = new Yaml().dump(car1);
        assertEquals(Util.getLocalResource("constructor/carwheel-root-map.yaml"), carYaml1);
        Map<Object, Object> car2 = (Map<Object, Object>) new Yaml().load(carYaml1);
        assertEquals(car1, car2);
        assertEquals(carYaml1, new Yaml().dump(car2));
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
        Wheel w1 = wheels.get(0);
        assertEquals(1, w1.getId());
        //
        String carYaml1 = new Yaml().dump(car);
        assertTrue(carYaml1.startsWith("!!org.yaml.snakeyaml.constructor.Car"));
        //
        Representer representer = new Representer();
        representer.addClassTag(Car.class, new Tag("!car"));
        yaml = new Yaml(representer);
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
