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
package org.yaml.snakeyaml.types;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

/**
 * @see http://yaml.org/type/bool.html
 */
public class BoolTagTest extends AbstractTest {
    public void testBool() {
        assertEquals(Boolean.TRUE, getMapValue("canonical: true", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("answer: NO", "answer"));
        assertEquals(Boolean.TRUE, getMapValue("logical: True", "logical"));
        assertEquals(Boolean.TRUE, getMapValue("option: on", "option"));
    }

    public void testBoolCanonical() {
        assertEquals(Boolean.TRUE, getMapValue("canonical: Yes", "canonical"));
        assertEquals(Boolean.TRUE, getMapValue("canonical: yes", "canonical"));
        assertEquals(Boolean.TRUE, getMapValue("canonical: YES", "canonical"));
        assertEquals("yES", getMapValue("canonical: yES", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("canonical: No", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("canonical: NO", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("canonical: no", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("canonical: off", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("canonical: Off", "canonical"));
        assertEquals(Boolean.FALSE, getMapValue("canonical: OFF", "canonical"));
        assertEquals(Boolean.TRUE, getMapValue("canonical: ON", "canonical"));
        assertEquals(Boolean.TRUE, getMapValue("canonical: On", "canonical"));
        assertEquals(Boolean.TRUE, getMapValue("canonical: on", "canonical"));
        // it looks like it is against the specification but it is like in
        // PyYAML
        assertEquals("n", getMapValue("canonical: n", "canonical"));
        assertEquals("N", getMapValue("canonical: N", "canonical"));
        assertEquals("y", getMapValue("canonical: y", "canonical"));
        assertEquals("Y", getMapValue("canonical: Y", "canonical"));
    }

    public void testBoolShorthand() {
        assertEquals(Boolean.TRUE, getMapValue("boolean: !!bool true", "boolean"));
    }

    public void testBoolTag() {
        assertEquals(Boolean.TRUE,
                getMapValue("boolean: !<tag:yaml.org,2002:bool> true", "boolean"));
    }

    public void testBoolOut() {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("boolean", Boolean.TRUE);
        String output = dump(map);
        assertTrue(output, output.contains("boolean: true"));
    }

    public void testBoolOutAsYes() {
        Yaml yaml = new Yaml(new BoolRepresenter("YES"));
        String output = yaml.dump(true);
        assertEquals("YES\n", output);
    }

    /**
     * test flow style
     */
    public void testBoolOutAsEmpty2() {
        Yaml yaml = new Yaml(new BoolRepresenter("on"));
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("aaa", false);
        map.put("bbb", true);
        String output = yaml.dump(map);
        assertEquals("{aaa: false, bbb: on}\n", output);
    }

    /**
     * test block style
     */
    public void testBoolOutAsEmpty3() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        Yaml yaml = new Yaml(new BoolRepresenter("True"), options);
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("aaa", false);
        map.put("bbb", true);
        String output = yaml.dump(map);
        assertEquals("aaa: false\nbbb: True\n", output);
    }

    private class BoolRepresenter extends Representer {
        private String value;

        public BoolRepresenter(String value) {
            super();
            this.value = value;
            this.representers.put(Boolean.class, new RepresentBool());
        }

        // possible values are here http://yaml.org/type/bool.html
        // y, Y, n, N should not be used
        private class RepresentBool implements Represent {
            public Node representData(Object data) {
                String v;
                if (Boolean.TRUE.equals(data)) {
                    v = value;
                } else {
                    v = "false";
                }
                return representScalar(Tag.BOOL, v);
            }
        }
    }
}
