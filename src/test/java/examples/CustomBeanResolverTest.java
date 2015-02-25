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
package examples;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * http://code.google.com/p/snakeyaml/issues/detail?id=75
 */
public class CustomBeanResolverTest extends TestCase {
    private final Pattern CUSTOM_PATTERN = Pattern.compile("\\d+%");

    public void testOnlyBigDecimal() {
        Yaml yaml = new Yaml(new BigBeanConstructor());
        Foo foo = (Foo) yaml.load("bar: 50\nbaz: 35%\nbas: 1250");
        assertEquals(50.0, foo.bar);
        assertEquals("0.35", foo.baz.toString());
        assertEquals("1250", foo.bas);
    }

    public void testPrimitive() {
        Yaml yaml = new Yaml(new BigBeanConstructor());
        Foo foo = (Foo) yaml.load("bar: 50%\nbaz: 35%\nbas: 1250%\nbaw: 35");
        assertEquals(0.5, foo.bar);
        assertEquals("0.35", foo.baz.toString());
        assertEquals("1250%", foo.bas);
        assertEquals("35", foo.baw.toString());
    }

    class BigBeanConstructor extends Constructor {
        public BigBeanConstructor() {
            super(Foo.class);
            yamlClassConstructors.put(NodeId.scalar, new ConstructBig());
        }

        private class ConstructBig extends ConstructScalar {
            public Object construct(Node node) {
                if (node.getType().equals(BigDecimal.class)) {
                    String val = (String) constructScalar((ScalarNode) node);
                    if (CUSTOM_PATTERN.matcher(val).matches()) {
                        return new BigDecimal(val.substring(0, val.length() - 1))
                                .divide(new BigDecimal(100));
                    }
                } else if (node.getType().isAssignableFrom(double.class)) {
                    String val = (String) constructScalar((ScalarNode) node);
                    if (CUSTOM_PATTERN.matcher(val).matches()) {
                        return new Double(val.substring(0, val.length() - 1)) / 100;
                    }
                }
                return super.construct(node);
            }
        }
    }

    public static class Foo {
        public double bar = 0;
        public BigDecimal baz;
        public BigDecimal baw;
        public String bas;
    }
}
