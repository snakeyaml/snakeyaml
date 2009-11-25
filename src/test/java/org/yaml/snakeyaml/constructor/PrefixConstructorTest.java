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

import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class PrefixConstructorTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void test1() {
        Yaml yaml = new Yaml(new Loader(new PrefixConstructor()));
        String input = "- !org.yaml.Foo 123\n- !org.yaml.Bar 456\n- !Immutable [aaa, bbb]";
        List<Extra> list = (List<Extra>) yaml.load(input);
        assertEquals(3, list.size());
        Extra foo = list.get(0);
        assertEquals("Foo", foo.getName());
        assertEquals("123", foo.getValue());
        //
        Extra bar = list.get(1);
        assertEquals("Bar", bar.getName());
        assertEquals("456", bar.getValue());
        //
        Extra immut = list.get(2);
        assertEquals("aaa", immut.getName());
        assertEquals("bbb", immut.getValue());
    }

    private class PrefixConstructor extends SafeConstructor {
        public PrefixConstructor() {
            // define tags which begin with !org.yaml.
            String prefix = "!org.yaml.";
            this.yamlMultiConstructors.put(prefix, new PrefixConstruct(prefix,
                    PrefixConstructor.this));
            this.yamlConstructors.put(null, new ConstructUnknown(PrefixConstructor.this));
        }
    }

    private class PrefixConstruct extends AbstractConstruct {
        private String prefix;
        private BaseConstructor con;

        public PrefixConstruct(String prefix, BaseConstructor con) {
            this.prefix = prefix;
            this.con = con;
        }

        public Object construct(Node node) {
            String suffix = node.getTag().substring(prefix.length());
            return new Extra(suffix, con.constructScalar((ScalarNode) node).toString());
        }
    }

    private class ConstructUnknown extends AbstractConstruct {
        private BaseConstructor con;

        public ConstructUnknown(BaseConstructor con) {
            this.con = con;
        }

        @SuppressWarnings("unchecked")
        public Object construct(Node node) {
            List<String> list = (List<String>) con.constructSequence((SequenceNode) node);
            return new Extra(list.get(0), list.get(1));
        }
    }

    private class Extra {
        private String name;
        private String value;

        public Extra(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }
}
