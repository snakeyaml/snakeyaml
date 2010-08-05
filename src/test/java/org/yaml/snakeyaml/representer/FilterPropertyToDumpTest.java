/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.SnakeYaml;
import org.yaml.snakeyaml.introspector.Property;

public class FilterPropertyToDumpTest extends TestCase {

    public void testFilterProperty() {
        BeanToRemoveProperty bean = new BeanToRemoveProperty();
        bean.setNumber(24);
        JavaBeanDumper d = new JavaBeanDumper(new MyRepresenter(), new DumperOptions());
        String dump = d.dump(bean);
        // System.out.println(dump);
        assertEquals(
                "!!org.yaml.snakeyaml.representer.FilterPropertyToDumpTest$BeanToRemoveProperty {number: 24}\n",
                dump);
    }

    public void testFilterProperty2() {
        BeanToRemoveProperty bean = new BeanToRemoveProperty();
        bean.setNumber(24);
        Dumper dumper = new Dumper(new MyRepresenter(), new DumperOptions());
        SnakeYaml yaml = new SnakeYaml(dumper);
        String dump = yaml.dump(bean);
        // System.out.println(dump);
        assertEquals(
                "!!org.yaml.snakeyaml.representer.FilterPropertyToDumpTest$BeanToRemoveProperty {number: 24}\n",
                dump);
        // include by default
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        yaml = new SnakeYaml(options);
        dump = yaml.dump(bean);
        // System.out.println(dump);
        assertEquals(
                "!!org.yaml.snakeyaml.representer.FilterPropertyToDumpTest$BeanToRemoveProperty {number: 24,\n  setTestCase: true}\n",
                dump);
    }

    public class BeanToRemoveProperty {
        private int number;

        public boolean isSetTestCase() {
            return true;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    private class MyRepresenter extends Representer {
        @Override
        protected Set<Property> getProperties(Class<? extends Object> type)
                throws IntrospectionException {
            Set<Property> set = super.getProperties(type);
            if (type.equals(BeanToRemoveProperty.class)) {
                // drop setTestCase property
                for (Property prop : set) {
                    if (prop.getName().equals("setTestCase")) {
                        set.remove(prop);
                        break;
                    }
                }
            }
            return set;
        }
    }
}
