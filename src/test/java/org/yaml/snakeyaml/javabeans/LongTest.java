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

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class LongTest extends TestCase {
    public void testLongFail() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        Yaml yaml = new Yaml(options);
        Foo foo = new Foo();
        String output = yaml.dump(foo);
        // System.out.println(output);
        try {
            yaml.load(output);
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("argument type mismatch"));
        }
    }

    public static class Foo {
        private Long bar = Long.valueOf(42L);

        public Long getBar() {
            return bar;
        }

        public void setBar(Long bar) {
            this.bar = bar;
        }
    }

    public void testLongRepresenter() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        Representer repr = new Representer();
        repr.addClassTag(Long.class, new Tag("!!java.lang.Long"));
        Yaml yaml = new Yaml(repr, options);

        Foo foo = new Foo();
        String output = yaml.dump(foo);
        // System.out.println(output);
        Foo foo2 = (Foo) yaml.load(output);
        assertEquals(new Long(42L), foo2.getBar());
    }

    public void testLongConstructor() {
        String doc = "!!org.yaml.snakeyaml.javabeans.LongTest$Foo\n\"bar\": !!int \"42\"";
        // System.out.println(doc);
        Yaml yaml = new Yaml();
        Foo foo2 = (Foo) yaml.load(doc);
        assertEquals(new Long(42L), foo2.getBar());
    }
}
