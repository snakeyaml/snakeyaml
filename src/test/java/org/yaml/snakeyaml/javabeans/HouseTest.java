/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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

package org.yaml.snakeyaml.javabeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HouseTest extends TestCase {
    /**
     * no root global tag
     */
    public void testDump1() {
        House house = new House();
        FrontDoor frontDoor = new FrontDoor("qaz1", 5);
        frontDoor.setKeytype("qwerty123");
        house.setFrontDoor(frontDoor);
        List<Room> rooms = new ArrayList<Room>();
        rooms.add(new Room("Hall"));
        rooms.add(new Room("Kitchen"));
        house.setRooms(rooms);
        Map<String, String> reminders = new TreeMap<String, String>();
        reminders.put("today", "do nothig");
        reminders.put("tomorrow", "go shoping");
        house.setReminders(reminders);
        house.setNumber(1);
        house.setStreet("Wall Street");
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        String yaml = beanDumper.dump(house);
        String etalon = Util.getLocalResource("javabeans/house-dump1.yaml");
        assertEquals(etalon, yaml);
        // false is default
        beanDumper = new JavaBeanDumper(false);
        String output2 = beanDumper.dump(house);
        assertEquals(etalon, output2);
        // load
        Yaml beanLoader = new Yaml();
        House loadedHouse = beanLoader.loadAs(yaml, House.class);
        assertNotNull(loadedHouse);
        assertEquals("Wall Street", loadedHouse.getStreet());
        // dump again
        String yaml3 = beanDumper.dump(loadedHouse);
        assertEquals(yaml, yaml3);
    }

    /**
     * with global root class tag (global tag should be avoided)
     */
    public void testDump3() {
        House house = new House();
        FrontDoor frontDoor = new FrontDoor("qaz1", 5);
        frontDoor.setKeytype("qwerty123");
        house.setFrontDoor(frontDoor);
        List<Room> rooms = new ArrayList<Room>();
        rooms.add(new Room("Hall"));
        rooms.add(new Room("Kitchen"));
        house.setRooms(rooms);
        Map<String, String> reminders = new TreeMap<String, String>();
        reminders.put("today", "do nothig");
        reminders.put("tomorrow", "go shoping");
        house.setReminders(reminders);
        house.setNumber(1);
        house.setStreet("Wall Street");
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        String yaml = beanDumper.dump(house);
        String etalon = Util.getLocalResource("javabeans/house-dump3.yaml");
        assertEquals(etalon, yaml);
        // load
        TypeDescription description = new TypeDescription(House.class);
        description.putListPropertyType("rooms", Room.class);
        Yaml beanLoader = new Yaml(new Constructor(description));
        House loadedHouse = (House) beanLoader.load(yaml);
        House loadedHouse2 = (House) beanLoader.loadAs(yaml, House.class);
        assertNotNull(loadedHouse);
        assertFalse(loadedHouse == loadedHouse2);
        assertEquals("Wall Street", loadedHouse.getStreet());
        assertEquals(1, loadedHouse.getNumber());
        assertEquals(1, loadedHouse2.getNumber());
        FrontDoor fdoor = loadedHouse.getFrontDoor();
        assertEquals(frontDoor.getId(), fdoor.getId());
        assertEquals(frontDoor.getHeight(), fdoor.getHeight());
        assertEquals(frontDoor.getKeytype(), fdoor.getKeytype());
        assertEquals(frontDoor, fdoor);
        assertEquals(reminders, loadedHouse.getReminders());
        List<Room> loadedRooms = loadedHouse.getRooms();
        assertEquals(rooms, loadedRooms);
        // dump again
        String yaml3 = beanDumper.dump(loadedHouse);
        assertEquals(yaml, yaml3);
    }

    /**
     * with global root class tag (global tag should be avoided)
     */
    public void testDump2() {
        House house = new House();
        FrontDoor frontDoor = new FrontDoor("qaz1", 5);
        frontDoor.setKeytype("qwerty123");
        house.setFrontDoor(frontDoor);
        List<Room> rooms = new ArrayList<Room>();
        rooms.add(new Room("Hall"));
        rooms.add(new Room("Kitchen"));
        house.setRooms(rooms);
        Map<String, String> reminders = new TreeMap<String, String>();
        reminders.put("today", "do nothig");
        reminders.put("tomorrow", "go shoping");
        house.setReminders(reminders);
        house.setNumber(1);
        house.setStreet("Wall Street");
        JavaBeanDumper beanDumper = new JavaBeanDumper(true);
        String yaml = beanDumper.dump(house);
        String etalon = Util.getLocalResource("javabeans/house-dump2.yaml");
        assertEquals(etalon, yaml);
        // load
        Yaml beanLoader = new Yaml();
        House loadedHouse = beanLoader.loadAs(yaml, House.class);
        assertNotNull(loadedHouse);
        assertEquals("Wall Street", loadedHouse.getStreet());
        // dump again
        String yaml3 = beanDumper.dump(loadedHouse);
        assertEquals(yaml, yaml3);
    }
}
