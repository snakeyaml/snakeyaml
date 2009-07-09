/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.recursive.generics;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HumanGenericsTest extends TestCase {

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
        String etalon = Util.getLocalResource("recursive/generics/no-children-1.yaml");
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
        Set<Human> children = new LinkedHashSet<Human>(2);
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putMapPropertyType("children", Human.class, Object.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children.yaml");
        assertEquals(etalon, output);
        //
        Human son2 = (Human) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getParner(), son2.getMother());
        assertSame(father2, son2.getMother().getParner());

        Set<Human> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getParner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(Human.class, child.getClass()); // check if type
                                                       // descriptor was correct
        }
    }

    public void testChildren2() throws IOException {
        Human2 father = new Human2();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        Human2 mother = new Human2();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setParner(mother);
        mother.setParner(father);
        mother.setBankAccountOwner(father);
        //
        Human2 son = new Human2();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        Human2 daughter = new Human2();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        HashMap<Human2, String> children = new LinkedHashMap<Human2, String>(2);
        children.put(son, "son");
        children.put(daughter, "daughter");
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(Human2.class);
        humanDescription.putMapPropertyType("children", Human2.class, String.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children-2.yaml");
        assertEquals(etalon, output);
        //
        Human2 son2 = (Human2) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human2 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getParner(), son2.getMother());
        assertSame(father2, son2.getMother().getParner());

        Map<Human2, String> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getParner().getChildren(), children2);

    }

    public void testChildren3() throws IOException {
        Human3 father = new Human3();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        Human3 mother = new Human3();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setParner(mother);
        mother.setParner(father);
        mother.setBankAccountOwner(father);
        //
        Human3 son = new Human3();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        Human3 daughter = new Human3();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        LinkedList<Human3> children = new LinkedList<Human3>();
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription Human3Description = new TypeDescription(Human3.class);
        Human3Description.putListPropertyType("children", Human3.class);
        constructor.addTypeDescription(Human3Description);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children-3.yaml");
        assertEquals(etalon, output);
        //
        Human3 son2 = (Human3) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human3 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getParner(), son2.getMother());
        assertSame(father2, son2.getMother().getParner());

        List<Human3> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getParner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(Human3.class, child.getClass()); // check if type
                                                        // descriptor was
                                                        // correct
        }
    }

    /*
     * Loads same structure as created in testChildren. But root object is set
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenSetAsRoot() throws IOException {
        String etalon = Util.getLocalResource("recursive/generics/with-children-as-set.yaml");

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putMapPropertyType("children", Human.class, Object.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        Set<Human> children2 = (Set<Human>) yaml.load(etalon);
        assertNotNull(children2);
        assertEquals(2, children2.size());

        Human firstChild = children2.iterator().next();

        Human father2 = firstChild.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", firstChild.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getParner(), firstChild.getMother());
        assertSame(father2, firstChild.getMother().getParner());

        assertSame(father2.getParner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(Human.class, child.getClass()); // check if type
                                                       // descriptor was correct
        }
    }

    /*
     * Loads same structure as created in testChildren. But root object is map
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenMapAsRoot() throws IOException {
        String etalon = Util.getLocalResource("recursive/generics/with-children-as-map.yaml");

        Constructor constructor = new Constructor();
        TypeDescription Human2Description = new TypeDescription(Human2.class);
        Human2Description.putMapPropertyType("children", Human2.class, String.class);
        constructor.addTypeDescription(Human2Description);

        Yaml yaml = new Yaml(new Loader(constructor));
        Map<Human2, String> children2 = (Map<Human2, String>) yaml.load(etalon);
        assertNotNull(children2);
        assertEquals(2, children2.size());

        Entry<Human2, String> firstEntry = children2.entrySet().iterator().next();
        Human2 firstChild = firstEntry.getKey();

        Human2 father2 = firstChild.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", firstChild.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getParner(), firstChild.getMother());
        assertSame(father2, firstChild.getMother().getParner());

        assertSame(father2.getParner().getChildren(), children2);
    }

    /*
     * Loads same structure as created in testChildren. But root object is list
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenListRoot() throws IOException {
        Human3 father = new Human3();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        Human3 mother = new Human3();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setParner(mother);
        mother.setParner(father);
        mother.setBankAccountOwner(father);
        //
        Human3 son = new Human3();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        Human3 daughter = new Human3();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        LinkedList<Human3> children = new LinkedList<Human3>();
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription Human3Description = new TypeDescription(Human3.class);
        Human3Description.putListPropertyType("children", Human3.class);
        constructor.addTypeDescription(Human3Description);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(father.getChildren());
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children-as-list.yaml");
        assertEquals(etalon, output);
        //
        List<Human3> children2 = (List<Human3>) yaml.load(output);
        assertNotNull(children2);
        Human3 son2 = children2.iterator().next();
        assertEquals(2, children2.size());

        Human3 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getParner(), son2.getMother());
        assertSame(father2, son2.getMother().getParner());

        assertSame(father2.getParner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(Human3.class, child.getClass()); // check if type
                                                        // descriptor was
                                                        // correct
        }
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
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/beanring-3.yaml");
        assertEquals(etalon, output);
        //
        Human loadedMan1 = (Human) yaml.load(output);
        assertNotNull(loadedMan1);
        assertEquals("Man 1", loadedMan1.getName());
        Human loadedMan2 = loadedMan1.getBankAccountOwner();
        Human loadedMan3 = loadedMan2.getBankAccountOwner();
        assertSame(loadedMan1, loadedMan3.getBankAccountOwner());
    }

}
