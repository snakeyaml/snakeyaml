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

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class IgnoreTagsExampleTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testLoad() {
        String input = Util.getLocalResource("examples/unknown-tags-example.yaml");
        // System.out.println(input);
        Yaml yaml = new Yaml(new MyConstructor());
        Map<String, Object> result = (Map<String, Object>) yaml.load(input);
        // Check the result
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("123", result.get("aaa"));
        //
        List<Object> bbb = (List<Object>) result.get("bbb");
        assertEquals(2, bbb.size());
        assertEquals(new Integer(111), bbb.get(0));
        assertEquals("ddd", bbb.get(1));
        //
        Map<String, Object> ccc = (Map<String, Object>) result.get("ccc");
        assertEquals(2, ccc.size());
        assertEquals(1.0, ccc.get("x"));
        assertEquals(3.1416, ccc.get("y"));
    }

    private class MyConstructor extends Constructor {
        private Construct original;

        public MyConstructor() {
            original = this.yamlConstructors.get(null);
            this.yamlConstructors.put(null, new IgnoringConstruct());
        }

        private class IgnoringConstruct extends AbstractConstruct {
            public Object construct(Node node) {
                if (node.getTag().startsWith("!KnownTag")) {
                    return original.construct(node);
                } else {
                    switch (node.getNodeId()) {
                    case scalar:
                        return yamlConstructors.get(Tag.STR).construct(node);
                    case sequence:
                        return yamlConstructors.get(Tag.SEQ).construct(node);
                    case mapping:
                        return yamlConstructors.get(Tag.MAP).construct(node);
                    default:
                        throw new YAMLException("Unexpected node");
                    }
                }
            }
        }
    }
}
