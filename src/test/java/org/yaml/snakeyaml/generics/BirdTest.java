package org.yaml.snakeyaml.generics;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanParser;
import org.yaml.snakeyaml.Yaml;

public class BirdTest extends TestCase {

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
        System.out.println(output);
        // no global tags must be emitted.
        assertEquals("home: !!org.yaml.snakeyaml.generics.Nest {height: 3}\nname: Eagle\n", output);

        Bird parsed = JavaBeanParser.load(
                "home: !!org.yaml.snakeyaml.generics.Nest {height: 3}\nname: Eagle", Bird.class);
        assertEquals(bird.getName(), parsed.getName());
        assertEquals(bird.getHome().getHeight(), parsed.getHome().getHeight());
        // the following line fails. The problem is that even though the runtime
        // is aware that the class of 'home' is Nest it sets 'Object.class'
        // as the class for the 'home' property (see
        // PropertyDescriptor.getPropertyType()).
        // The implementation of Java Generics leaves much to be desired.
        // JavaBeanParser.load("home: {height: 3}\nname: Eagle", Bird.class);
    }
}
