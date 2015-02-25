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
package org.yaml.snakeyaml.recursive;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HumanTest extends TestCase {

    public void testNoChildren() {
        Human father = new Human();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        Human mother = new Human();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        Yaml yaml = new Yaml();
        String output = yaml.dump(father);
        String etalon = Util.getLocalResource("recursive/no-children-1.yaml");
        assertEquals(etalon, output);
        //
        Human father2 = (Human) yaml.load(output);
        assertNotNull(father2);
        assertEquals("Father", father2.getName());
        assertEquals("Mother", father2.getPartner().getName());
        assertEquals("Father", father2.getBankAccountOwner().getName());
        assertSame(father2, father2.getBankAccountOwner());
    }

    public void testNoChildrenPretty() {
        Human father = new Human();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        Human mother = new Human();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(FlowStyle.FLOW);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(father);
        String etalon = Util.getLocalResource("recursive/no-children-1-pretty.yaml");
        assertEquals(etalon, output);
        //
        Human father2 = (Human) yaml.load(output);
        assertNotNull(father2);
        assertEquals("Father", father2.getName());
        assertEquals("Mother", father2.getPartner().getName());
        assertEquals("Father", father2.getBankAccountOwner().getName());
        assertSame(father2, father2.getBankAccountOwner());
    }

    public void testChildren() {
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
        father.setPartner(mother);
        mother.setPartner(father);
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
        Yaml beanDumper = new Yaml();
        String output = beanDumper.dumpAsMap(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children.yaml");
        assertEquals(etalon, output);
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putMapPropertyType("children", Human.class, Object.class);
        Yaml beanLoader = new Yaml(new Constructor(humanDescription));
        //
        Human son2 = beanLoader.loadAs(output, Human.class);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        Set<Human> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            // check if type descriptor was correct
            assertSame(Human.class, child.getClass());
        }

        // check if hashCode is correct
        validateSet(children2);
    }

    public void testChildrenPretty() {
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
        father.setPartner(mother);
        mother.setPartner(father);
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
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.FLOW);
        options.setPrettyFlow(true);
        Yaml beanDumper = new Yaml(options);
        String output = beanDumper.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children-pretty.yaml");
        assertEquals(etalon, output);
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putMapPropertyType("children", Human.class, Object.class);
        Yaml beanLoader = new Yaml(new Constructor(humanDescription));
        //
        Human son2 = beanLoader.loadAs(output, Human.class);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        Set<Human> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            // check if type descriptor was correct
            assertSame(Human.class, child.getClass());
        }

        // check if hashCode is correct
        validateSet(children2);
    }

    public void testChildren2() {
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
        father.setPartner(mother);
        mother.setPartner(father);
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

        Constructor constructor = new Constructor(Human2.class);
        TypeDescription humanDescription = new TypeDescription(Human2.class);
        humanDescription.putMapPropertyType("children", Human2.class, String.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(constructor);
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children-2.yaml");
        assertEquals(etalon, output);
        //
        Human2 son2 = (Human2) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human2 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        Map<Human2, String> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        validateMapKeys(children2);
    }

    public void testChildren3() {
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
        father.setPartner(mother);
        mother.setPartner(father);
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
        ArrayList<Human3> children = new ArrayList<Human3>();
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor(Human3.class);
        TypeDescription Human3Description = new TypeDescription(Human3.class);
        Human3Description.putListPropertyType("children", Human3.class);
        constructor.addTypeDescription(Human3Description);

        Yaml yaml = new Yaml(constructor);
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children-3.yaml");
        assertEquals(etalon, output);
        //
        Human3 son2 = (Human3) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human3 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        List<Human3> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            // check if type descriptor was correct
            assertSame(Human3.class, child.getClass());
        }
    }

    /*
     * Loads same structure as created in testChildren. But root object is set
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenSetAsRoot() {
        String etalon = Util.getLocalResource("recursive/with-children-as-set.yaml");

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putMapPropertyType("children", Human.class, Object.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(constructor);
        Set<Human> children2 = (Set<Human>) yaml.load(etalon);
        assertNotNull(children2);
        assertEquals(2, children2.size());

        Human firstChild = children2.iterator().next();

        Human father2 = firstChild.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", firstChild.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), firstChild.getMother());
        assertSame(father2, firstChild.getMother().getPartner());

        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            // check if type descriptor was correct
            assertSame(Human.class, child.getClass());
        }

        validateSet(children2);
    }

    /*
     * Loads same structure as created in testChildren. But root object is map
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenMapAsRoot() {
        String etalon = Util.getLocalResource("recursive/with-children-as-map.yaml");

        Constructor constructor = new Constructor();
        TypeDescription Human2Description = new TypeDescription(Human2.class);
        Human2Description.putMapPropertyType("children", Human2.class, String.class);
        constructor.addTypeDescription(Human2Description);

        Yaml yaml = new Yaml(constructor);
        Map<Human2, String> children2 = (Map<Human2, String>) yaml.load(etalon);
        assertNotNull(children2);
        assertEquals(2, children2.size());

        Entry<Human2, String> firstEntry = children2.entrySet().iterator().next();
        Human2 firstChild = firstEntry.getKey();

        Human2 father2 = firstChild.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", firstChild.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), firstChild.getMother());
        assertSame(father2, firstChild.getMother().getPartner());

        assertSame(father2.getPartner().getChildren(), children2);

        validateMapKeys(children2);
    }

    /*
     * Loads same structure as created in testChildren. But root object is list
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenListRoot() {
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
        father.setPartner(mother);
        mother.setPartner(father);
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
        ArrayList<Human3> children = new ArrayList<Human3>();
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription Human3Description = new TypeDescription(Human3.class);
        Human3Description.putListPropertyType("children", Human3.class);
        constructor.addTypeDescription(Human3Description);

        Yaml yaml = new Yaml(constructor);
        String output = yaml.dump(father.getChildren());
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children-as-list.yaml");
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
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            // check if type descriptor was correct
            assertSame(Human3.class, child.getClass());
        }
    }

    public void testBeanRing() {
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

    public void qtestCollectionRing() {
        // Set<Object> set = new HashSet<Object>();
        // List<Object> list = new ArrayList<Object>();
        // Map<Object, Object> map = new HashMap<Object, Object>();
        // set.add(list);
        // list.add(map);
        // map.put("1", set);
        // //
        // try {
        // Yaml yaml = new Yaml();
        // String output = yaml.dump(set);
        // // String etalon = Util.getLocalResource("recursive/???.yaml");
        // // assertEquals(etalon, output);
        // //
        // // Set<Object> loadedSet = (Set<Object>) yaml.load(output);
        // } catch (StackOverflowError e) {
        // fail("Cannot dump recursive collections.");
        // }
    }

    /**
     * Checks if object was put into the set after full construction. So the
     * hashCode was calculated correctly (if it depends on internal object's
     * state).
     * 
     * @param set
     */
    private void validateSet(Set<?> set) {
        for (Object object : set) {
            assertTrue(set.contains(object));
        }
    }

    /**
     * Checks if object was put into the map as key after full construction. So
     * the hashCode was calculated correctly (if it depends on internal object's
     * state).
     * 
     * @param map
     */
    private void validateMapKeys(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            assertTrue(map.containsKey(entry.getKey()));
        }
    }

    public void testChildrenWithoutRootTag() {
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
        father.setPartner(mother);
        mother.setPartner(father);
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
        Yaml beanDumper = new Yaml();
        String output = beanDumper.dumpAsMap(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/with-children-no-root-tag.yaml");
        assertEquals(etalon, output);
        TypeDescription humanDescription = new TypeDescription(Human.class);
        humanDescription.putMapPropertyType("children", Human.class, Object.class);
        Yaml beanLoader = new Yaml(new Constructor(humanDescription));
        //
        Human son2 = beanLoader.loadAs(output, Human.class);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        Human father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        Set<Human> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            // check if type descriptor was correct
            assertSame(Human.class, child.getClass());
        }

        // check if hashCode is correct
        validateSet(children2);
    }
}
