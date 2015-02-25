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
package org.yaml.snakeyaml.issues.issue29;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.representer.Representer;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=29
 */
public class FlexibleScalarStyleTest extends TestCase {
    public void testLong() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.FOLDED);
        Yaml yaml = new Yaml(options);
        String result = yaml
                .dump("qqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqq "
                        + "qqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqq "
                        + "qqqqqqqqqqqqqqqqqqqqqqqqq 111111111111111111111111\n "
                        + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqqqq\n");
        // System.out.println(result);
        assertTrue(result.startsWith(">\n"));
        assertEquals(
                ">\n  qqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqq\n  qqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqq 111111111111111111111111\n   qqqqqqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqqqq\n",
                result);
    }

    public void testNoFoldedScalar() {
        DumperOptions options = new DumperOptions();
        options.setWidth(30);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(getData());
        // System.out.println(output);
        String etalon = Util.getLocalResource("representer/scalar-style1.yaml");
        assertEquals(etalon, output);
    }

    public void testCustomScalarStyle() {
        DumperOptions options = new DumperOptions();
        options.setWidth(30);
        Yaml yaml = new Yaml(new MyRepresenter(), options);
        String output = yaml.dump(getData());
        // System.out.println(output);
        String etalon = Util.getLocalResource("representer/scalar-style2.yaml");
        assertEquals(etalon, output);
    }

    public void testCustomScalarStyleNoSplitLines() {
        DumperOptions options = new DumperOptions();
        options.setWidth(30);
        options.setSplitLines(false);
        Yaml yaml = new Yaml(new MyRepresenter(), options);
        String output = yaml.dump(getData());
        // System.out.println(output);
        String etalon = Util.getLocalResource("representer/scalar-style3.yaml");
        assertEquals(etalon, output);
    }

    private Map<String, String> getData() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("name", "Steve Jobs");
        map.put("address", "Name\nStreet Number\nCountry");
        map.put("description",
                "1111111111 2222222222 3333333333 4444444444 5555555555 6666666666 7777777777 8888888888 9999999999 0000000000");
        return map;
    }

    private class MyRepresenter extends Representer {

        public MyRepresenter() {
            super();
            this.representers.put(String.class, new FlexibleRepresent());
        }

        private class FlexibleRepresent extends RepresentString {
            public Node representData(Object data) {
                ScalarNode node = (ScalarNode) super.representData(data);
                if (node.getStyle() == null) {
                    // if Plain scalar style
                    if (node.getValue().length() < 25) {
                        return node;
                    } else {
                        // Folded scalar style
                        return new ScalarNode(node.getTag(), node.getValue(), node.getStartMark(),
                                node.getEndMark(), '>');
                    }
                } else {
                    return node;
                }
            }
        }
    }
}
