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
package org.yaml.snakeyaml.issues.issue124;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

public class DumpTest extends TestCase {

    public void testDumperOptionsSideEffect() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        String output0 = yaml.dump(bean);
        // System.out.println(output0);
        assertEquals("!!org.yaml.snakeyaml.issues.issue124.Bean124\na: aaa\nnumbers: [1, 2, 3]\n",
                output0);
        String output1 = yaml.dumpAsMap(bean);
        // System.out.println(output1);
        assertEquals("a: aaa\nnumbers:\n- 1\n- 2\n- 3\n", output1);
        String output2 = yaml.dump(bean);
        // System.out.println(output2);
        assertEquals("Yaml.dumpAs() should not have any side effects.", output0, output2);
    }

    public void testDumperDifferentTag() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        String output1 = yaml.dumpAs(bean, new Tag("!!foo.bar"), FlowStyle.BLOCK);
        assertEquals("!!foo.bar\na: aaa\nnumbers:\n- 1\n- 2\n- 3\n", output1);
    }

    public void testDumperFlowStyle() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        String output1 = yaml.dumpAs(bean, new Tag("!!foo.bar"), FlowStyle.FLOW);
        assertEquals("!!foo.bar {a: aaa, numbers: [1, 2, 3]}\n", output1);
    }

    public void testDumperAutoStyle() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        String output1 = yaml.dumpAs(bean, new Tag("!!foo.bar"), FlowStyle.AUTO);
        assertEquals("!!foo.bar\na: aaa\nnumbers: [1, 2, 3]\n", output1);
    }

    public void testDumperNullStyle() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        String output1 = yaml.dumpAs(bean, new Tag("!!foo.bar"), null);
        assertEquals("!!foo.bar\na: aaa\nnumbers: [1, 2, 3]\n", output1);
    }

    public void testDumperNullStyle2() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        Bean124 bean = new Bean124();
        String output1 = yaml.dumpAs(bean, new Tag("!!foo2.bar2"), null);
        assertEquals("!!foo2.bar2\na: aaa\nnumbers:\n- 1\n- 2\n- 3\n", output1);
    }

    public void testDumperNullTag() {
        Yaml yaml = new Yaml();
        Bean124 bean = new Bean124();
        String output1 = yaml.dumpAs(bean, null, FlowStyle.BLOCK);
        assertEquals(
                "!!org.yaml.snakeyaml.issues.issue124.Bean124\na: aaa\nnumbers:\n- 1\n- 2\n- 3\n",
                output1);
    }
}