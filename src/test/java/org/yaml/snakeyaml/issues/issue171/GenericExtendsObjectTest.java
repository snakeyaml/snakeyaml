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
package org.yaml.snakeyaml.issues.issue171;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class GenericExtendsObjectTest extends TestCase {

    public void testNoCompilationError() throws Exception {
        Yaml yaml = new Yaml(new CustomRepresenter());
        ClassWithGenericMap map = new ClassWithGenericMap();
        List<Integer> list = new ArrayList<Integer>();
        list.add(7);
        map.services.put("wow", list);
        String output = yaml.dump(map);
        assertEquals("wow: [7]\n", output);
    }
}
