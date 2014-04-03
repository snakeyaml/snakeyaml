/**
 * Copyright (c) 2008-2014, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.issues.issue150;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlLoadAsIssueTest {

    private StringBuilder doc;

    @Before
    public void setUp() {
        doc = new StringBuilder();
        line("!car");
        line("plate: 12-XP-F4");
        line("wheels:");
        line("- w#1");
        line("- w#2");
        line("- w#3");
        line("- w#4");
    }

    private StringBuilder line(String text) {
        return doc.append(text).append('\n');
    }

    @Test
    public void loadConstructsDocumentCorrectly() {
        Yaml yaml = yaml();
        Object result = yaml.load(reader());
        assertNotNull(result);
        assertEquals(Car.class, result.getClass());
        assertEquals("12-XP-F4", ((Car) result).getPlate());
        assertEquals(4, ((Car) result).getWheels().size());
    }

    private Yaml yaml() {
        Yaml yaml = new Yaml(new MyConstructor());
        yaml.addImplicitResolver(new Tag("!wheel"), Pattern.compile("w#\\d+"), "w");
        return yaml;
    }

    @Test
    public void ignoreImplicitTag() {
        Yaml yaml = yaml();
        try {
            yaml.loadAs(reader(), Car.class);
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage(),
                    e.getMessage().startsWith("Cannot create property=wheels for JavaBean"));
            assertTrue(
                    e.getCause().getMessage(),
                    e.getCause()
                            .getMessage()
                            .startsWith(
                                    "No single argument constructor found for class org.yaml.snakeyaml.issues.issue150.Wheel"));
        }
    }

    private Reader reader() {
        return new StringReader(doc.toString());
    }

    private class MyConstructor extends Constructor {
        public MyConstructor() {
            yamlConstructors.put(new Tag("!car"), new ConstructCar());
            yamlConstructors.put(new Tag("!wheel"), new ConstructWheel());
        }

        private String toScalarString(Node node) {
            return (String) constructScalar((ScalarNode) node);
        }

        private class ConstructCar extends AbstractConstruct {

            @SuppressWarnings("unchecked")
            public Car construct(Node node) {
                Car car = new Car();
                MappingNode mapping = (MappingNode) node;
                List<NodeTuple> list = mapping.getValue();
                for (NodeTuple tuple : list) {
                    String field = toScalarString(tuple.getKeyNode());
                    if ("plate".equals(field)) {
                        car.setPlate(toScalarString(tuple.getValueNode()));
                    }
                    if ("wheels".equals(field)) {
                        SequenceNode snode = (SequenceNode) tuple.getValueNode();
                        List<Wheel> wheels = (List<Wheel>) constructSequence(snode);
                        car.setWheels(wheels);
                    }
                }
                return car;
            }
        }

        private class ConstructWheel extends AbstractConstruct {

            public Wheel construct(Node node) {
                Wheel w = null;
                String strValue = toScalarString(node);
                if (strValue != null && strValue.length() > 2) {
                    strValue = strValue.trim().substring(2);
                    w = new Wheel();
                    w.setId(Integer.valueOf(strValue));
                }
                return w;
            }
        }
    }
}
