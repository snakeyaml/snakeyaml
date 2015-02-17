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
package org.yaml.snakeyaml.representer;

import java.beans.IntrospectionException;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;

public class FilterPropertyToDumpTest extends TestCase {

    public void testFilterPropertyInJavaBeanDumper() {
        BeanToRemoveProperty bean = new BeanToRemoveProperty();
        bean.setNumber(24);
        bean.setId("ID124");
        Yaml d = new Yaml();
        String dump = d.dumpAsMap(bean);
        // System.out.println(dump);
        assertEquals("id: ID124\nnumber: 24\n", dump);
    }

    public void testFilterPropertyInYaml() {
        BeanToRemoveProperty bean = new BeanToRemoveProperty();
        bean.setNumber(25);
        bean.setId("ID125");
        Yaml yaml = new Yaml(new MyRepresenter());
        String dump = yaml.dumpAsMap(bean);
        // System.out.println(dump);
        assertEquals("number: 25\n", dump);
    }

    public void testDoNotFilterPropertyIncludeReadOnly() {
        BeanToRemoveProperty bean = new BeanToRemoveProperty();
        bean.setNumber(26);
        bean.setId("ID126");
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
        String dump = yaml.dump(bean);
        // System.out.println(dump);
        assertEquals(
                "!!org.yaml.snakeyaml.representer.FilterPropertyToDumpTest$BeanToRemoveProperty {id: ID126,\n  number: 26, something: true}\n",
                dump);
    }

    public class BeanToRemoveProperty {
        private int number;
        private String id;

        public boolean isSomething() {
            return true;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    private class MyRepresenter extends Representer {
        @Override
        protected Set<Property> getProperties(Class<? extends Object> type)
                throws IntrospectionException {
            Set<Property> set = super.getProperties(type);
            Set<Property> filtered = new TreeSet<Property>();
            if (type.equals(BeanToRemoveProperty.class)) {
                // filter properties
                for (Property prop : set) {
                    String name = prop.getName();
                    if (!name.equals("id")) {
                        filtered.add(prop);
                    }
                }
            }
            return filtered;
        }
    }
}
