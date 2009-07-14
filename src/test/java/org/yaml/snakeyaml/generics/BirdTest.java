package org.yaml.snakeyaml.generics;

import java.beans.IntrospectionException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanParser;
import org.yaml.snakeyaml.Yaml;

public class BirdTest extends TestCase {

    public void testHome() throws IntrospectionException {
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
        String javaVendor = System.getProperty("java.vm.name");
        if (JvmDetector.isProperIntrospection()) {
            // no global tags
            System.out.println("java.vm.name: " + javaVendor);
            assertEquals("no global tags must be emitted.", "home: {height: 3}\nname: Eagle\n",
                    output);
            parsed = JavaBeanParser.load(output, Bird.class);

        } else {
            // with global tags
            System.out
                    .println("JDK requires global tags for JavaBean properties with Java Generics. java.vm.name: "
                            + javaVendor);
            assertEquals("global tags are inevitable here.",
                    "home: !!org.yaml.snakeyaml.generics.Nest {height: 3}\nname: Eagle\n", output);
            parsed = JavaBeanParser.load(output, Bird.class);
        }
        assertEquals(bird.getName(), parsed.getName());
        assertEquals(bird.getHome().getHeight(), parsed.getHome().getHeight());
    }
}
