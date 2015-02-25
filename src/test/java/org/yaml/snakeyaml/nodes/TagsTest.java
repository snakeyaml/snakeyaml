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
package org.yaml.snakeyaml.nodes;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class TagsTest extends TestCase {

    public void testGetGlobalTagForClass() {
        assertEquals(new Tag("tag:yaml.org,2002:java.lang.String"), new Tag(String.class));
        assertEquals(new Tag("tag:yaml.org,2002:org.yaml.snakeyaml.nodes.TagsTest"), new Tag(
                TagsTest.class));
    }

    /**
     * test fix for issue 18 -
     * http://code.google.com/p/snakeyaml/issues/detail?id=18
     */
    public void testLong() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        Yaml yaml = new Yaml(options);
        Foo foo = new Foo();
        String output = yaml.dump(foo);
        // System.out.println(output);
        Foo foo2 = (Foo) yaml.load(output);
        assertEquals(new Long(42L), foo2.getBar());
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
}
