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
package org.yaml.snakeyaml.serializer;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.ArrayList;
import java.util.List;

public class AnchorGeneratorTest extends TestCase {

    public void testNext() {
        AnchorGenerator generator = new NumberAnchorGenerator(0);
        assertEquals("id001", generator.nextAnchor(null));
        assertEquals("id002", generator.nextAnchor(null));
    }

    public void testCustomGenerator() {
        List<Object> list = new ArrayList<Object>();
        list.add("data123");
        list.add(list);
        Yaml yaml1 = new Yaml();
        String output = yaml1.dump(list);
        assertEquals("&id001\n" +
                "- data123\n" +
                "- *id001\n", output);


        DumperOptions options = new DumperOptions();
        Yaml yaml2 = new Yaml(options);
        options.setAnchorGenerator(new Gener(3));
        String output2 = yaml2.dump(list);
        assertEquals("&list-id004\n" +
                "- data123\n" +
                "- *list-id004\n", output2);
    }

    class Gener extends NumberAnchorGenerator {

        public Gener(int lastAnchorId) {
            super(lastAnchorId);
        }

        public String nextAnchor(Node node) {
            if (node.getTag() == Tag.SEQ)
                return "list-" + super.nextAnchor(node);
            else
                return super.nextAnchor(node);
        }
    }
}
