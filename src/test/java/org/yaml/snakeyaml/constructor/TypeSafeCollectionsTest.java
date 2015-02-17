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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class TypeSafeCollectionsTest extends TestCase {

    public void testTypeSafeList() {
        Constructor constructor = new Constructor(Car.class);
        TypeDescription carDescription = new TypeDescription(Car.class);
        carDescription.putListPropertyType("wheels", Wheel.class);
        constructor.addTypeDescription(carDescription);
        Yaml yaml = new Yaml(constructor);
        Car car = (Car) yaml.load(Util.getLocalResource("constructor/car-no-root-class.yaml"));
        assertEquals("12-XP-F4", car.getPlate());
        List<Wheel> wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.size());
        for (Wheel wheel : wheels) {
            assertTrue(wheel.getId() > 0);
        }
    }

    public void testTypeSafeMap() {
        Constructor constructor = new Constructor(MyCar.class);
        TypeDescription carDescription = new TypeDescription(MyCar.class);
        carDescription.putMapPropertyType("wheels", MyWheel.class, Object.class);
        constructor.addTypeDescription(carDescription);
        Yaml yaml = new Yaml(constructor);
        MyCar car = (MyCar) yaml.load(Util
                .getLocalResource("constructor/car-no-root-class-map.yaml"));
        assertEquals("00-FF-Q2", car.getPlate());
        Map<MyWheel, Date> wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.size());
        for (MyWheel wheel : wheels.keySet()) {
            assertTrue(wheel.getId() > 0);
            Date date = wheels.get(wheel);
            long time = date.getTime();
            assertTrue("It must be midnight.", time % 10000 == 0);
        }
    }

    public void testWithGlobalTag() {
        Map<MyWheel, Date> wheels = new TreeMap<MyWheel, Date>();
        long time = 1248212168084L;
        for (int i = 1; i < 6; i++) {
            MyWheel mw = new MyWheel();
            mw.setId(i);
            mw.setBrand(mw.getBrand() + String.valueOf(i));
            wheels.put(mw, new Date(time + i));
        }
        MyCar c = new MyCar();
        c.setPlate("00-FF-Q2");
        c.setWheels(wheels);
        Representer representer = new Representer();
        representer.addClassTag(MyWheel.class, Tag.MAP);
        Yaml yaml = new Yaml(representer);
        String output = yaml.dump(c);
        assertEquals(Util.getLocalResource("javabeans/mycar-with-global-tag1.yaml"), output);
        // load
        Yaml beanLoader = new Yaml();
        MyCar car = beanLoader.loadAs(output, MyCar.class);
        assertNotNull(car);
        assertEquals("00-FF-Q2", car.getPlate());
        assertEquals(5, car.getWheels().size());
        for (Date d : car.getWheels().values()) {
            // give a day for any timezone
            assertTrue(d.before(new Date(time + 1000 * 60 * 60 * 24)));
            assertTrue(d.after(new Date(time)));
        }
        Object wheel = car.getWheels().keySet().iterator().next();
        assertTrue(wheel instanceof MyWheel);
        MyWheel w = (MyWheel) wheel;
        assertEquals(1, w.getId());
        assertEquals("Pirelli1", w.getBrand());
    }
}
