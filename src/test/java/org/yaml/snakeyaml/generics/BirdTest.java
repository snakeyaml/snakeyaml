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

package org.yaml.snakeyaml.generics;

import java.beans.IntrospectionException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

public class BirdTest extends TestCase {

    public void testHome() throws IntrospectionException {
        Bird bird = new Bird();
        bird.setName("Eagle");
        Nest home = new Nest();
        home = new Nest();
        home.setHeight(3);
        bird.setHome(home);
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot(Tag.MAP);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(bird);
        Bird parsed;
        String javaVendor = System.getProperty("java.vm.name");
        JavaBeanLoader<Bird> loader = new JavaBeanLoader<Bird>(Bird.class);
        if (GenericsBugDetector.isProperIntrospection()) {
            // no global tags
            System.out.println("java.vm.name: " + javaVendor);
            assertEquals("no global tags must be emitted.", "home: {height: 3}\nname: Eagle\n",
                    output);
            parsed = loader.load(output);

        } else {
            // with global tags
            System.out
                    .println("JDK requires global tags for JavaBean properties with Java Generics. java.vm.name: "
                            + javaVendor);
            assertEquals("global tags are inevitable here.",
                    "home: !!org.yaml.snakeyaml.generics.Nest {height: 3}\nname: Eagle\n", output);
            parsed = loader.load(output);
        }
        assertEquals(bird.getName(), parsed.getName());
        assertEquals(bird.getHome().getHeight(), parsed.getHome().getHeight());
    }
}
