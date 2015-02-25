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
package org.yaml.snakeyaml.issues.issue46;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

/**
 * Issue 46: Dump a java.io.File object
 */
public class FileTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void test() {
        File file = new File("src/test/resources/examples/list-bean-1.yaml");
        assertTrue(file.exists());
        Yaml yaml = new Yaml(new MyRepresenter());
        Map<String, File> map = new HashMap<String, File>();
        map.put("one", file);
        String output = yaml.dump(map);
        // System.out.println(output);
        assertTrue(output, output.startsWith("{one: !!java.io.File '"));
        assertTrue(output, output.endsWith("list-bean-1.yaml'}\n"));
        Map<String, File> parsed = (Map<String, File>) yaml.load(output);
        File file2 = parsed.get("one");
        assertTrue(file2.getAbsolutePath(), file2.getAbsolutePath().endsWith("list-bean-1.yaml"));
    }

    public class MyRepresenter extends Representer {
        public MyRepresenter() {
            this.representers.put(File.class, new FileRepresenter());
        }

        public class FileRepresenter implements Represent {
            public Node representData(Object data) {
                File file = (File) data;
                Node scalar = representScalar(new Tag("!!java.io.File"), file.getAbsolutePath());
                return scalar;
            }
        }
    }
}
