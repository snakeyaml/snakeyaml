package org.yaml.snakeyaml.javabeans;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;

public class HouseTest extends TestCase {
    /**
     * with global root class tag (global tag should be avoided)
     */
    public void testDump3() {
        House house = new House();
        FrontDoor frontDoor = new FrontDoor("qaz1", 5);
        frontDoor.setKeytype("qwerty123");
        house.setFrontDoor(frontDoor);
        List<Room> rooms = new LinkedList<Room>();
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
        beanDumper.setMapTagForBean(Room.class);
        String yaml = beanDumper.dump(house);
        System.out.println(yaml);
        String etalon = Util.getLocalResource("javabeans/house-dump3.yaml");
        assertEquals(etalon, yaml);
        // load
        TypeDescription description = new TypeDescription(House.class);
        description.putListPropertyType("rooms", Room.class);
        JavaBeanLoader<House> beanLoader = new JavaBeanLoader<House>(description);
        House loadedHouse = beanLoader.load(yaml);
        assertNotNull(loadedHouse);
        assertEquals("Wall Street", loadedHouse.getStreet());
        assertEquals(1, loadedHouse.getNumber());
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
     * no root global tag
     */
    public void testDump1() {
        House house = new House();
        FrontDoor frontDoor = new FrontDoor("qaz1", 5);
        frontDoor.setKeytype("qwerty123");
        house.setFrontDoor(frontDoor);
        List<Room> rooms = new LinkedList<Room>();
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
        JavaBeanLoader<House> beanLoader = new JavaBeanLoader<House>(House.class);
        House loadedHouse = beanLoader.load(yaml);
        assertNotNull(loadedHouse);
        assertEquals("Wall Street", loadedHouse.getStreet());
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
        List<Room> rooms = new LinkedList<Room>();
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
        JavaBeanLoader<House> beanLoader = new JavaBeanLoader<House>(House.class);
        House loadedHouse = beanLoader.load(yaml);
        assertNotNull(loadedHouse);
        assertEquals("Wall Street", loadedHouse.getStreet());
        // dump again
        String yaml3 = beanDumper.dump(loadedHouse);
        assertEquals(yaml, yaml3);
    }
}
