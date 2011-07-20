/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.issues.issue124;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

public class DumpSideEffectsTest extends TestCase {

    public void testDumperOptionsSideEffect() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        bean.setA("aaa");
        bean.setB("bbb");
        String output0 = yaml.dump(bean);
        // System.out.println(output0);
        assertEquals("!!org.yaml.snakeyaml.issues.issue124.Bean124 {a: aaa, b: bbb}\n", output0);
        String output1 = yaml.dumpAs(bean, Tag.MAP);
        // System.out.println(output1);
        assertEquals("a: aaa\nb: bbb\n", output1);
        String output2 = yaml.dump(bean);
        // System.out.println(output2);
        assertEquals("Yaml.dumpAs() should not have any side effects.", output0, output2);
    }
}