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
package org.yaml.snakeyaml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

public class CollectionWithBeanYamlTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testYamlMap() {
        Map<String, Bean> data = new TreeMap<String, Bean>();
        data.put("gold1", new Bean());
        data.put("gold2", new Bean());

        Yaml yaml = new Yaml();
        String output = yaml.dump(data);
        assertEquals(
                "gold1: !!org.yaml.snakeyaml.CollectionWithBeanYamlTest$Bean {a: ''}\ngold2: !!org.yaml.snakeyaml.CollectionWithBeanYamlTest$Bean {a: ''}\n",
                output);
        Object o = yaml.load(output);

        assertTrue(o instanceof Map);
        Map<String, Bean> m = (Map<String, Bean>) o;
        assertTrue(m.get("gold1") instanceof Bean);
        assertTrue("" + m.get("gold2").getClass(), m.get("gold2") instanceof Bean);
    }

    @SuppressWarnings("unchecked")
    public void testYamlList() {
        List<Bean> data = new ArrayList<Bean>();
        data.add(new Bean("1"));
        data.add(new Bean("2"));

        Yaml yaml = new Yaml();
        String output = yaml.dump(data);
        assertEquals(
                "- !!org.yaml.snakeyaml.CollectionWithBeanYamlTest$Bean {a: '1'}\n- !!org.yaml.snakeyaml.CollectionWithBeanYamlTest$Bean {a: '2'}\n",
                output);
        Object o = yaml.load(output);

        assertTrue(o instanceof List);
        List<Bean> m = (List<Bean>) o;
        assertEquals(2, m.size());
        assertTrue(m.get(0) instanceof Bean);
        assertTrue(m.get(1) instanceof Bean);
    }

    public static class Bean {
        private String a;

        public Bean() {
            a = "";
        }

        public Bean(String value) {
            a = value;
        }

        public String getA() {
            return a;
        }

        public void setA(String s) {
            a = s;
        }
    }
}
