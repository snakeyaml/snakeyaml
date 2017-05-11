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

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class ReferencesTest {

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
        Map map = (Map) yaml.load(data);
        assertNotNull(map);
    }
}