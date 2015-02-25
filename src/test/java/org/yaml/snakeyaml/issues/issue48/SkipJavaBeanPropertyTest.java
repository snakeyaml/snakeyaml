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
package org.yaml.snakeyaml.issues.issue48;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class SkipJavaBeanPropertyTest extends TestCase {
    public void testWithNull() {
        Bean bean = new Bean();
        bean.setValue(3);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals("name: null\nvalue: 3\n", output);
    }

    public void testWithoutNull() {
        Bean bean = new Bean();
        bean.setValue(5);
        Yaml yaml = new Yaml(new MyRepresenter());
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals("value: 5\n", output);
    }

    private class MyRepresenter extends Representer {
        @Override
        protected NodeTuple representJavaBeanProperty(Object bean, Property property, Object value,
                Tag customTag) {
            if (value != null) {
                return super.representJavaBeanProperty(bean, property, value, customTag);
            } else {
                return null;
            }
        }
    }
}
