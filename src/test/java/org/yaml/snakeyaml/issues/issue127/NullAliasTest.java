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
package org.yaml.snakeyaml.issues.issue127;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class NullAliasTest extends TestCase {
    private static final Tag MY_TAG = new Tag("tag:example.com,2011:bean");

    public void testRespresenter() {
        Bean bean = new Bean();

        bean.setA("a"); // leave b null
        Yaml yaml = new Yaml(new BeanRepresenter());
        String output = yaml.dump(bean);
        assertEquals("!<tag:example.com,2011:bean>\na: a\nb: null\n", output);
    }

    class BeanRepresenter extends Representer {
        public BeanRepresenter() {
            this.representers.put(Bean.class, new RepresentBean());
        }

        private class RepresentBean implements Represent {
            public Node representData(Object data) {
                Bean bean = (Bean) data;
                Map<String, Object> fields = new LinkedHashMap<String, Object>(2);
                fields.put("a", bean.getA());
                fields.put("b", bean.getB());
                return representMapping(MY_TAG, fields, false);
            }
        }
    }
}