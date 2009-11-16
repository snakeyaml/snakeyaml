/**
 * Copyright (c) 2008-2009 Andrey Somov
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

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

public class FilterClassesConstructorTest extends TestCase {

    public void testGetClassForName() {
        Yaml yaml = new Yaml(new Loader(new FilterConstructor(true)));
        try {
            yaml
                    .load("!!org.yaml.snakeyaml.constructor.FilterClassesConstructorTestt$LoaderBean {name: Andrey, number: 555}");
            fail("Filter is expected.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.constructor.FilterClassesConstructorTestt$LoaderBean; exception=Filter is applied.",
                    e.getMessage());
        }
        yaml = new Yaml(new Loader(new FilterConstructor(false)));
        LoaderBean s = (LoaderBean) yaml
                .load("!!org.yaml.snakeyaml.constructor.FilterClassesConstructorTest$LoaderBean {name: Andrey, number: 555}");
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

    public static class LoaderBean {
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
