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
package org.yaml.snakeyaml.issues.issue310;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class PropertyWithPrivateCostructorTest {

    public static class OptionRepresenter extends Representer {

        public OptionRepresenter() {
            this.representers.put(Option.class, new RepresentOption());
        }

        private class RepresentOption implements Represent {
            public Node representData(Object data) {
                Option<?> opt = (Option<?>) data;
                return represent(opt.getValue());
            }
        }

    }

    @Test
    public void loadFromString() {

        String yamlStr = "id: 123\n" + "income: 123456.78\n" + "name: Neo Anderson";

        Person loadedPerson = yaml().loadAs(yamlStr, Person.class);

        assertEquals("id", loadedPerson.getId(), 123);
        assertEquals("name", loadedPerson.getName(), "Neo Anderson");
        assertEquals("income", loadedPerson.getIncome().getValue().doubleValue(), 123456.78, 0.);
    }

    @Test
    public void dumpNload() {

        Person person = new Person(123, "Neo Anderson", Option.valueOf(123456.78));

        String dump = yaml().dumpAsMap(person);

        Person loadedPerson = yaml().loadAs(dump, Person.class);

        assertEquals("id", loadedPerson.getId(), 123);
        assertEquals("name", loadedPerson.getName(), "Neo Anderson");
        assertEquals("income", loadedPerson.getIncome().getValue().doubleValue(), 123456.78, 0.);
    }

    private Yaml yaml() {
        Yaml _yaml = new Yaml(new OptionRepresenter());
        _yaml.setBeanAccess(BeanAccess.FIELD);
        return _yaml;
    }
}
