/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tags;
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

    @SuppressWarnings("unchecked")
    public void testWithGlobalTag() throws IOException {
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
        representer.addClassTag(MyWheel.class, Tags.MAP);
        Dumper dumper = new Dumper(representer, new DumperOptions());
        Yaml yaml = new Yaml(dumper);
        String output = yaml.dump(c);
        assertEquals(Util.getLocalResource("javabeans/mycar-with-global-tag1.yaml"), output);
        // load
        JavaBeanLoader<MyCar> beanLoader = new JavaBeanLoader<MyCar>(MyCar.class);
        MyCar car = beanLoader.load(output);
        assertNotNull(car);
        assertEquals("00-FF-Q2", car.getPlate());
        assertEquals(5, car.getWheels().size());
        for (Date d : car.getWheels().values()) {
            // give a day for any timezone
            assertTrue(d.before(new Date(time + 1000 * 60 * 60 * 24)));
            assertTrue(d.after(new Date(time)));
        }
        // due to erasure MyCar gets maps instead of MyWheels
        Object wheel = car.getWheels().keySet().iterator().next();
        assertTrue(wheel instanceof Map);
        Map<String, Object> map = (Map<String, Object>) wheel;
        assertEquals(new Integer(1), (Integer) map.get("id"));
        assertEquals("Pirelli1", map.get("brand"));
    }
}
