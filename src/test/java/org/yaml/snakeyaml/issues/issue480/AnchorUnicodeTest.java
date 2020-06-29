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
package org.yaml.snakeyaml.issues.issue480;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.serializer.AnchorGenerator;

import java.util.ArrayList;
import java.util.List;

public class AnchorUnicodeTest extends TestCase {

    public void testAnchor() {
        DumperOptions options = new DumperOptions();
        options.setAnchorGenerator(new AnchorGenerator() {
            int id = 0;

            @Override
            public String nextAnchor(Node node) {
                return "タスク" + id++;
            }
        });

        Yaml yaml = new Yaml(options);

        List<String> list = new ArrayList<>();
        list.add("abc");

        List<List<String>> toExport = new ArrayList<>();
        toExport.add(list);
        toExport.add(list);

        yaml.dump(toExport);
    }
}
