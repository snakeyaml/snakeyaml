/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HumanTest extends TestCase {

    public void testNoChildren() throws IOException {
        Human father = new Human();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        Human mother = new Human();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setParner(mother);
        mother.setParner(father);
        mother.setBankAccountOwner(father);
        Yaml yaml = new Yaml();
        String output = yaml.dump(father);
        String etalon = Util.getLocalResource("recursive/no-children-1.yaml");
        assertEquals(etalon, output);
        //
        Human father2 = (Human) yaml.load(output);
        assertNotNull(father2);
        assertEquals("Father", father2.getName());
        assertEquals("Mother", father2.getParner().getName());
        assertEquals("Father", father2.getBankAccountOwner().getName());
        assertSame(father2, father2.getBankAccountOwner());
    }

    public void testChildren() throws IOException {
        Human father = new Human();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        Human mother = new Human();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setParner(mother);
        mother.setParner(father);
        mother.setBankAccountOwner(father);
        //
        Human son = new Human();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        Human daughter = new Human();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        List<Human> children = new ArrayList<Human>(2);
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putListPropertyType("children", Human.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(father);
        System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children-2.yaml");
        assertEquals(etalon, output);
        //
        Human father2 = (Human) yaml.load(output);
        assertNotNull(father2);
        assertEquals("Father", father2.getName());
        assertEquals("Mother", father2.getParner().getName());
        assertEquals("Father", father2.getBankAccountOwner().getName());
        assertSame(father2, father2.getBankAccountOwner());
    }

    public void testBeanRing() throws IOException {
        Human man1 = new Human();
        man1.setName("Man 1");
        Human man2 = new Human();
        man2.setName("Man 2");
        Human man3 = new Human();
        man3.setName("Man 3");
        man1.setBankAccountOwner(man2);
        man2.setBankAccountOwner(man3);
        man3.setBankAccountOwner(man1);
        //
        Yaml yaml = new Yaml();
        String output = yaml.dump(man1);
        System.out.println(output);
        String etalon = Util.getLocalResource("recursive/beanring-3.yaml");
        assertEquals(etalon, output);
        //
        Human loadedMan1 = (Human) yaml.load(output);
        assertNotNull(loadedMan1);
        assertEquals("Man 1", loadedMan1.getName());
        Human loadedMan2 = loadedMan1.getBankAccountOwner();
        Human loadedMan3 = loadedMan2.getBankAccountOwner();
        assertSame(loadedMan1, loadedMan3.getBankAccountOwner());
    }

    // TODO Java's hashcode leaves much to be desired
    public void qtestCollectionRing() throws IOException {
        Set<Object> set = new HashSet<Object>();
        List<Object> list = new ArrayList<Object>();
        Map<Object, Object> map = new HashMap<Object, Object>();
        set.add(list);
        list.add(map);
        map.put("1", set);
        //
        try {
            Yaml yaml = new Yaml();
            String output = yaml.dump(set);
            // String etalon = Util.getLocalResource("recursive/???.yaml");
            // assertEquals(etalon, output);
            //
            // Set<Object> loadedSet = (Set<Object>) yaml.load(output);
        } catch (StackOverflowError e) {
            fail("Cannot dump recursive collections.");
        }
    }
}
