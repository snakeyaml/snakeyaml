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
package org.yaml.snakeyaml.javabeans;

import java.util.Arrays;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class StringArrayTest extends TestCase {
    public void testStrings() {
        A a = new A();
        a.setNames(new String[] { "aaa", "bbb", "ccc" });
        Yaml yaml = new Yaml();
        String output = yaml.dump(a);
        assertEquals("!!org.yaml.snakeyaml.javabeans.StringArrayTest$A\nnames: [aaa, bbb, ccc]\n",
                output);
        A b = (A) yaml.load(output);
        assertTrue(Arrays.equals(a.getNames(), b.getNames()));
    }

    public void testStringsPretty() {
        A a = new A();
        a.setNames(new String[] { "aaa", "bbb", "ccc" });
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(a);
        assertEquals(
                "!!org.yaml.snakeyaml.javabeans.StringArrayTest$A\nnames: [\n  aaa,\n  bbb,\n  ccc]\n",
                output);
        A b = (A) yaml.load(output);
        assertTrue(Arrays.equals(a.getNames(), b.getNames()));
    }

    public static class A {
        String[] names;

        public String[] getNames() {
            return names;
        }

        public void setNames(String[] names) {
            this.names = names;
        }

        public String getName(int index) {
            return names[index];
        }

        public void setName(int index, String name) {
            this.names[index] = name;
        }
    }
}
