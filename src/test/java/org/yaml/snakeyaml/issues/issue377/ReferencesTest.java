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
package org.yaml.snakeyaml.issues.issue377;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ReferencesTest {

    /**
     * https://en.wikipedia.org/wiki/Billion_laughs_attack#Variations
     */
    @Test
    public void billionLaughsAttack() {
        String data = "a: &a [\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\"]\n" +
                "b: &b [*a,*a,*a,*a,*a,*a,*a,*a,*a]\n" +
                "c: &c [*b,*b,*b,*b,*b,*b,*b,*b,*b]\n" +
                "d: &d [*c,*c,*c,*c,*c,*c,*c,*c,*c]\n" +
                "e: &e [*d,*d,*d,*d,*d,*d,*d,*d,*d]\n" +
                "f: &f [*e,*e,*e,*e,*e,*e,*e,*e,*e]\n" +
                "g: &g [*f,*f,*f,*f,*f,*f,*f,*f,*f]\n" +
                "h: &h [*g,*g,*g,*g,*g,*g,*g,*g,*g]\n" +
                "i: &i [*h,*h,*h,*h,*h,*h,*h,*h,*h]";
        Yaml yaml = new Yaml();
        Map map = yaml.load(data);
        assertNotNull(map);
    }

    @Test
    public void referencesAttack() {
        HashMap root = new HashMap();
        HashMap s1, s2, t1, t2;
        s1 = root;
        s2 = new HashMap();
        long time1 = System.currentTimeMillis();
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
        int SIZE = 25;
        for (int i = 0; i < SIZE; i++) {

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

        //FIXME
        // this is VERY BAD code
        // the map has itself as a key (no idea why it may be used)
        HashMap f = new HashMap();
        f.put(f, "a");
        f.put("g", root);

        Yaml yaml = new Yaml(new SafeConstructor());
        String output = yaml.dump(f);
        //System.out.println(output);

        // Load
        yaml.load(output);
        long time2 = System.currentTimeMillis();
        System.out.println("Time was " + ((time2 - time1) / 1000) + " seconds.");
    }
}