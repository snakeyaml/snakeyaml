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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * https://en.wikipedia.org/wiki/Billion_laughs_attack#Variations
 */
public class BillionLaughsAttackTest {
    public static final String data = "a: &a [\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\",\"lol\"]\n" +
            "b: &b [*a,*a,*a,*a,*a,*a,*a,*a,*a]\n" +
            "c: &c [*b,*b,*b,*b,*b,*b,*b,*b,*b]\n" +
            "d: &d [*c,*c,*c,*c,*c,*c,*c,*c,*c]\n" +
            "e: &e [*d,*d,*d,*d,*d,*d,*d,*d,*d]\n" +
            "f: &f [*e,*e,*e,*e,*e,*e,*e,*e,*e]\n" +
            "g: &g [*f,*f,*f,*f,*f,*f,*f,*f,*f]\n" +
            "h: &h [*g,*g,*g,*g,*g,*g,*g,*g,*g]\n" +
            "i: &i [*h,*h,*h,*h,*h,*h,*h,*h,*h]";

    public static final String scalarAliasesData = "a: &a foo\n" +
            "b:  *a\n" +
            "c:  *a\n" +
            "d:  *a\n" +
            "e:  *a\n" +
            "f:  *a\n" +
            "g:  *a\n";

    @Test
    public void billionLaughsAttackLoaded() {
        LoaderOptions settings = new LoaderOptions();
        settings.setMaxAliasesForCollections(72);
        Yaml yaml = new Yaml(settings);
        Map map = (Map) yaml.load(data);
        assertNotNull(map);
    }

    @Test
    public void billionLaughsAttackExpanded() {
    	LoaderOptions settings = new LoaderOptions();
        settings.setMaxAliasesForCollections(100);
        Yaml yaml = new Yaml(settings);
        Map map = (Map) yaml.load(data);
        assertNotNull(map);
        try {
            map.toString();
            fail("Expected overflow");
        } catch (Throwable e) {
            assertTrue("Catched exception " + e +
                " is not an instance of OutOfMemoryError", e instanceof OutOfMemoryError);
        }
    }

    @Test
    public void billionLaughsAttackWithRestrictedAliases() {
    	LoaderOptions settings = new LoaderOptions();
        Yaml yaml = new Yaml(settings);
        try {
            yaml.load(data);
            fail();
        } catch (YAMLException e) {
            assertEquals("Number of aliases for non-scalar nodes exceeds the specified max=50", e.getMessage());
        }
    }

    @Test
    public void doNotRestrictScalarAliases() {
    	LoaderOptions settings = new LoaderOptions();
        settings.setMaxAliasesForCollections(5); //smaller than number of aliases for scalars
        Yaml yaml = new Yaml(settings);
        Map map = (Map) yaml.load(scalarAliasesData);
        assertNotNull(map);
    }
    
}