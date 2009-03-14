/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.util.Date;
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
import org.yaml.snakeyaml.representer.Representer;

public class TypeSafeCollectionsTest extends TestCase {

    public void testTypeSafeList() throws IOException {
        Constructor constructor = new Constructor(Car.class);
        TypeDescription carDescription = new TypeDescription(Car.class);
        carDescription.putListPropertyType("wheels", Wheel.class);
        constructor.addTypeDescription(carDescription);
        Loader loader = new Loader(constructor);
        Yaml yaml = new Yaml(loader);
        Car car = (Car) yaml.load(Util.getLocalResource("constructor/car-no-root-class.yaml"));
        assertEquals("12-XP-F4", car.getPlate());
        List<Wheel> wheels = car.getWheels();
        assertNotNull(wheels);
        assertEquals(5, wheels.size());
        for (Wheel wheel : wheels) {
            assertTrue(wheel.getId() > 0);
        }
    }

    public void testTypeSafeMap() throws IOException {
        Constructor constructor = new Constructor(MyCar.class);
        TypeDescription carDescription = new TypeDescription(MyCar.class);
        carDescription.putMapPropertyType("wheels", MyWheel.class, Object.class);
        constructor.addTypeDescription(carDescription);
        Loader loader = new Loader(constructor);
        Yaml yaml = new Yaml(loader);
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

    public void test1() throws IOException {
        Map<MyWheel, Date> wheels = new HashMap<MyWheel, Date>();
        for (int i = 1; i < 6; i++) {
            MyWheel mw = new MyWheel();
            mw.setId(i);
            if (i == 2) {
                mw.setBrand("Michel");
            }
            wheels.put(mw, new Date());
        }
        MyCar c = new MyCar();
        c.setPlate("00-FF-Q2");
        c.setWheels(wheels);
        Representer representer = new Representer();
        representer.addTypeDescription(new TypeDescription(MyWheel.class, "tag:yaml.org,2002:map"));
        Dumper dumper = new Dumper(representer, new DumperOptions());
        Yaml yaml = new Yaml(dumper);
        String output = yaml.dump(c);
        System.out.println(output);
    }
}
