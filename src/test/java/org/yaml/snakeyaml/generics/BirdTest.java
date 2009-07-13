package org.yaml.snakeyaml.generics;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanParser;
import org.yaml.snakeyaml.Yaml;

public class BirdTest extends TestCase {
    private boolean correctJVM;

    @Override
    protected void setUp() throws Exception {
        // non all JVM implementations can properly recognize property classes
        String javaVendor = System.getProperty("java.vm.name");
        if (javaVendor.contains("OpenJDK")) {
            correctJVM = true;
            System.out.println("JDK: " + javaVendor);
        } else {
            correctJVM = false;
            System.out
                    .println("JDK requires global tags for JavaBean properties with Java Generics: "
                            + javaVendor);
        }
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

    public void testHome() {
        Bird bird = new Bird();
        bird.setName("Eagle");
        Nest home = new Nest();
        home = new Nest();
        home.setHeight(3);
        bird.setHome(home);
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot("tag:yaml.org,2002:map");
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(bird);
        Bird parsed;
        if (correctJVM) {
            // no global tags
            assertEquals("no global tags must be emitted.", "home: {height: 3}\nname: Eagle\n",
                    output);
             parsed = JavaBeanParser.load(output, Bird.class);
           
        } else {
            // with global tags
            assertEquals("global tags are inevitable here.",
                    "home: !!org.yaml.snakeyaml.generics.Nest {height: 3}\nname: Eagle\n", output);
             parsed = JavaBeanParser.load(output, Bird.class);
        }
        assertEquals(bird.getName(), parsed.getName());
        assertEquals(bird.getHome().getHeight(), parsed.getHome().getHeight());
    }
}
