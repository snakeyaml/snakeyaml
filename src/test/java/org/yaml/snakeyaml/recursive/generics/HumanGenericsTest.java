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

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanParser;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class HumanGenericsTest extends TestCase {
    private boolean skip = false;

    @Override
    protected void setUp() throws Exception {
        // non Sun implementations may fail
        String javaVendor = System.getProperty("java.vm.name");
        skip = !javaVendor.contains("HotSpot");
        /*
         * Properties props = System.getProperties(); Map<String, String> map =
         * new TreeMap<String, String>(); for (Object iterable_element :
         * props.keySet()) { map.put(iterable_element.toString(),
         * props.getProperty(iterable_element.toString())); }
         * 
         * for (Object iterable_element : map.keySet()) {
         * System.out.println("Key=" + iterable_element + " - " +
         * props.getProperty(iterable_element.toString())); }
         */
    }

    public void testNoChildren() throws IOException {
        if (skip) {
            return;
        }
        HumanGen father = new HumanGen();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        HumanGen mother = new HumanGen();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        Yaml yaml = new Yaml();
        String output = yaml.dump(father);
        String etalon = Util.getLocalResource("recursive/generics/no-children-1.yaml");
        assertEquals(etalon, output);
        //
        HumanGen father2 = (HumanGen) yaml.load(output);
        assertNotNull(father2);
        assertEquals("Father", father2.getName());
        assertEquals("Mother", father2.getPartner().getName());
        assertEquals("Father", father2.getBankAccountOwner().getName());
        assertSame(father2, father2.getBankAccountOwner());
    }

    /**
     * the YAML document should contain no global tags
     */
    public void testNoChildren2() throws IOException {
        if (skip) {
            return;
        }
        HumanGen father = new HumanGen();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        HumanGen mother = new HumanGen();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot("tag:yaml.org,2002:map");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(father);
        // TODO the YAML document should contain no global tags
        String etalon = Util.getLocalResource("recursive/generics/no-children-2.yaml");
        assertEquals(etalon, output);
        //
        HumanGen father2 = (HumanGen) JavaBeanParser.load(etalon, HumanGen.class);
        assertNotNull(father2);
        assertEquals("Father", father2.getName());
        assertEquals("Mother", father2.getPartner().getName());
        assertEquals("Father", father2.getBankAccountOwner().getName());
        assertSame(father2, father2.getBankAccountOwner());
    }

    public void testChildren() throws IOException {
        if (skip) {
            return;
        }
        HumanGen father = new HumanGen();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        HumanGen mother = new HumanGen();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        //
        HumanGen son = new HumanGen();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        HumanGen daughter = new HumanGen();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        Set<HumanGen> children = new LinkedHashSet<HumanGen>(2);
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(HumanGen.class);
        humanDescription.putMapPropertyType("children", HumanGen.class, Object.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children.yaml");
        assertEquals(etalon, output);
        //
        HumanGen son2 = (HumanGen) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        HumanGen father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        Set<HumanGen> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(HumanGen.class, child.getClass()); // check if type
            // descriptor was correct
        }
    }

    public void testChildren2() throws IOException {
        if (skip) {
            return;
        }
        HumanGen2 father = new HumanGen2();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        HumanGen2 mother = new HumanGen2();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        //
        HumanGen2 son = new HumanGen2();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        HumanGen2 daughter = new HumanGen2();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        HashMap<HumanGen2, String> children = new LinkedHashMap<HumanGen2, String>(2);
        children.put(son, "son");
        children.put(daughter, "daughter");
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(HumanGen2.class);
        humanDescription.putMapPropertyType("children", HumanGen2.class, String.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children-2.yaml");
        assertEquals(etalon, output);
        //
        HumanGen2 son2 = (HumanGen2) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        HumanGen2 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        Map<HumanGen2, String> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

    }

    public void testChildren3() throws IOException {
        if (skip) {
            return;
        }
        HumanGen3 father = new HumanGen3();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        HumanGen3 mother = new HumanGen3();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        //
        HumanGen3 son = new HumanGen3();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        HumanGen3 daughter = new HumanGen3();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        LinkedList<HumanGen3> children = new LinkedList<HumanGen3>();
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription Human3Description = new TypeDescription(HumanGen3.class);
        Human3Description.putListPropertyType("children", HumanGen3.class);
        constructor.addTypeDescription(Human3Description);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(son);
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children-3.yaml");
        assertEquals(etalon, output);
        //
        HumanGen3 son2 = (HumanGen3) yaml.load(output);
        assertNotNull(son2);
        assertEquals("Son", son.getName());

        HumanGen3 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        List<HumanGen3> children2 = father2.getChildren();
        assertEquals(2, children2.size());
        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(HumanGen3.class, child.getClass()); // check if type
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
        if (skip) {
            return;
        }
        String etalon = Util.getLocalResource("recursive/generics/with-children-as-set.yaml");

        Constructor constructor = new Constructor();
        TypeDescription humanDescription = new TypeDescription(HumanGen.class);
        humanDescription.putMapPropertyType("children", HumanGen.class, Object.class);
        constructor.addTypeDescription(humanDescription);

        Yaml yaml = new Yaml(new Loader(constructor));
        Set<HumanGen> children2 = (Set<HumanGen>) yaml.load(etalon);
        assertNotNull(children2);
        assertEquals(2, children2.size());

        HumanGen firstChild = children2.iterator().next();

        HumanGen father2 = firstChild.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", firstChild.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), firstChild.getMother());
        assertSame(father2, firstChild.getMother().getPartner());

        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(HumanGen.class, child.getClass()); // check if type
            // descriptor was correct
        }
    }

    /*
     * Loads same structure as created in testChildren. But root object is map
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenMapAsRoot() throws IOException {
        if (skip) {
            return;
        }
        String etalon = Util.getLocalResource("recursive/generics/with-children-as-map.yaml");

        Constructor constructor = new Constructor();
        TypeDescription Human2Description = new TypeDescription(HumanGen2.class);
        Human2Description.putMapPropertyType("children", HumanGen2.class, String.class);
        constructor.addTypeDescription(Human2Description);

        Yaml yaml = new Yaml(new Loader(constructor));
        Map<HumanGen2, String> children2 = (Map<HumanGen2, String>) yaml.load(etalon);
        assertNotNull(children2);
        assertEquals(2, children2.size());

        Entry<HumanGen2, String> firstEntry = children2.entrySet().iterator().next();
        HumanGen2 firstChild = firstEntry.getKey();

        HumanGen2 father2 = firstChild.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", firstChild.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), firstChild.getMother());
        assertSame(father2, firstChild.getMother().getPartner());

        assertSame(father2.getPartner().getChildren(), children2);
    }

    /*
     * Loads same structure as created in testChildren. But root object is list
     * of children
     */
    @SuppressWarnings("unchecked")
    public void testChildrenListRoot() throws IOException {
        if (skip) {
            return;
        }
        HumanGen3 father = new HumanGen3();
        father.setName("Father");
        father.setBirthday(new Date(1000000000));
        father.setBirthPlace("Leningrad");
        father.setBankAccountOwner(father);
        //
        HumanGen3 mother = new HumanGen3();
        mother.setName("Mother");
        mother.setBirthday(new Date(100000000000L));
        mother.setBirthPlace("Saint-Petersburg");
        father.setPartner(mother);
        mother.setPartner(father);
        mother.setBankAccountOwner(father);
        //
        HumanGen3 son = new HumanGen3();
        son.setName("Son");
        son.setBirthday(new Date(310000000000L));
        son.setBirthPlace("Munich");
        son.setBankAccountOwner(father);
        son.setFather(father);
        son.setMother(mother);
        //
        HumanGen3 daughter = new HumanGen3();
        daughter.setName("Daughter");
        daughter.setBirthday(new Date(420000000000L));
        daughter.setBirthPlace("New York");
        daughter.setBankAccountOwner(father);
        daughter.setFather(father);
        daughter.setMother(mother);
        //
        LinkedList<HumanGen3> children = new LinkedList<HumanGen3>();
        children.add(son);
        children.add(daughter);
        father.setChildren(children);
        mother.setChildren(children);
        //

        Constructor constructor = new Constructor();
        TypeDescription Human3Description = new TypeDescription(HumanGen3.class);
        Human3Description.putListPropertyType("children", HumanGen3.class);
        constructor.addTypeDescription(Human3Description);

        Yaml yaml = new Yaml(new Loader(constructor));
        String output = yaml.dump(father.getChildren());
        // System.out.println(output);
        String etalon = Util.getLocalResource("recursive/generics/with-children-as-list.yaml");
        assertEquals(etalon, output);
        //
        List<HumanGen3> children2 = (List<HumanGen3>) yaml.load(output);
        assertNotNull(children2);
        HumanGen3 son2 = children2.iterator().next();
        assertEquals(2, children2.size());

        HumanGen3 father2 = son2.getFather();
        assertEquals("Father", father2.getName());
        assertEquals("Mother", son2.getMother().getName());
        assertSame(father2, father2.getBankAccountOwner());
        assertSame(father2.getPartner(), son2.getMother());
        assertSame(father2, son2.getMother().getPartner());

        assertSame(father2.getPartner().getChildren(), children2);

        for (Object child : children2) {
            assertSame(HumanGen3.class, child.getClass()); // check if type
            // descriptor was
            // correct
        }
    }

    public void testBeanRing() throws IOException {
        if (skip) {
            return;
        }
        HumanGen man1 = new HumanGen();
        man1.setName("Man 1");
        HumanGen man2 = new HumanGen();
        man2.setName("Man 2");
        HumanGen man3 = new HumanGen();
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
        HumanGen loadedMan1 = (HumanGen) yaml.load(output);
        assertNotNull(loadedMan1);
        assertEquals("Man 1", loadedMan1.getName());
        HumanGen loadedMan2 = loadedMan1.getBankAccountOwner();
        HumanGen loadedMan3 = loadedMan2.getBankAccountOwner();
        assertSame(loadedMan1, loadedMan3.getBankAccountOwner());
    }
}
