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
package org.yaml.snakeyaml.constructor;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class FilterClassesConstructorTest extends TestCase {

    public void testGetClassForName() {
        Yaml yaml = new Yaml(new FilterConstructor(true));
        String input = "!!org.yaml.snakeyaml.constructor.FilterClassesConstructorTest$FilteredBean {name: Andrey, number: 543}";
        try {
            yaml.load(input);
            fail("Filter is expected.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Filter is applied."));
        }
        yaml = new Yaml(new FilterConstructor(false));
        FilteredBean s = (FilteredBean) yaml.load(input);
        assertEquals("Andrey", s.getName());
    }

    class FilterConstructor extends Constructor {
        private boolean filter;

        public FilterConstructor(boolean f) {
            filter = f;
        }

        @Override
        protected Class<?> getClassForName(String name) throws ClassNotFoundException {
            if (filter && name.startsWith("org.yaml")) {
                throw new RuntimeException("Filter is applied.");
            }
            return super.getClassForName(name);
        }
    }

    public static class FilteredBean {
        private String name;
        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
