/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue377;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ReferencesTest {

    /**
     * Create data which is difficult to parse.
     *
     * @param size - size of the map, defines the complexity
     * @return YAML to parse
     */
    private String createDump(int size) {
        HashMap root = new HashMap();
        HashMap s1, s2, t1, t2;
        s1 = root;
        s2 = new HashMap();
        /*
        the time to parse grows very quickly
        SIZE -> time to parse in seconds
        25 -> 1
        26 -> 2
        27 -> 3
        28 -> 8
        29 -> 13
        30 -> 28
        31 -> 52
        32 -> 113
        33 -> 245
        34 -> 500
         */
        for (int i = 0; i < size; i++) {

            t1 = new HashMap();
            t2 = new HashMap();
            t1.put("foo", "1");
            t2.put("bar", "2");

            s1.put("a", t1);
            s1.put("b", t2);
            s2.put("a", t1);
            s2.put("b", t2);

            s1 = t1;
            s2 = t2;
        }

        // this is VERY BAD code
        // the map has itself as a key (no idea why it may be used except of a DoS attack)
        HashMap f = new HashMap();
        f.put(f, "a");
        f.put("g", root);

        Yaml yaml = new Yaml(new SafeConstructor());
        String output = yaml.dump(f);
        return output;
    }

    @Test
    public void referencesWithRecursiveKeysNotAllowedByDefault() {
        String output = createDump(30);
        //System.out.println(output);
        long time1 = System.currentTimeMillis();
        // Load
        LoaderOptions settings = new LoaderOptions();
        settings.setMaxAliasesForCollections(150);
        Yaml yaml = new Yaml(settings);
        try {
            yaml.load(output);
            fail();
        } catch (Exception e) {
            assertEquals("Recursive key for mapping is detected but it is not configured to be allowed.", e.getMessage());
        }
        long time2 = System.currentTimeMillis();
        float duration = (time2 - time1) / 1000;
        assertTrue("It should fail quickly. Time was " + duration + " seconds.", duration < 1.0);
    }

    @Test
    public void parseManyAliasesForCollections() {
        String output = createDump(25);
        // Load
        // long time1 = System.currentTimeMillis();
        LoaderOptions settings = new LoaderOptions();
        settings.setMaxAliasesForCollections(50);
        settings.setAllowRecursiveKeys(true);
        Yaml yaml = new Yaml(settings);
        yaml.load(output);
        // Disabling this as it runs slower than 0.9 on my machine
        // long time2 = System.currentTimeMillis();
        // double duration = (time2 - time1) / 1000.0;
        // assertTrue("It should take time. Time was " + duration + " seconds.", duration > 0.9);
        // assertTrue("Time was " + duration + " seconds.", duration < 5.0);
    }

    @Test
    public void referencesWithRestrictedAliases() {
        // without alias restriction this size should occupy tons of CPU, memory and time to parse
        String bigYAML = createDump(35);
        // Load
        long time1 = System.currentTimeMillis();
        LoaderOptions settings = new LoaderOptions();
        settings.setMaxAliasesForCollections(40);
        settings.setAllowRecursiveKeys(true);
        Yaml yaml = new Yaml(settings);
        try {
            yaml.load(bigYAML);
            fail();
        } catch (Exception e) {
            assertEquals("Number of aliases for non-scalar nodes exceeds the specified max=40", e.getMessage());
        }
        long time2 = System.currentTimeMillis();
        float duration = (time2 - time1) / 1000;
        assertTrue("It should fail quickly. Time was " + duration + " seconds.", duration < 1.0);
    }

}