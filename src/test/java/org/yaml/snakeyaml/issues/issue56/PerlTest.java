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
package org.yaml.snakeyaml.issues.issue56;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class PerlTest extends TestCase {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testMaps() {
        Yaml yaml = new Yaml(new CustomConstructor());
        String input = Util.getLocalResource("issues/issue56-1.yaml");
        int counter = 0;
        for (Object obj : yaml.loadAll(input)) {
            // System.out.println(obj);
            Map<String, Object> map = (Map<String, Object>) obj;
            Integer oid = (Integer) map.get("oid");
            if (oid == 123058) {
                ArrayList a = (ArrayList) map.get("sequences");
                LinkedHashMap b = (LinkedHashMap) a.get(0);
                LinkedHashMap c = (LinkedHashMap) b.get("atc");
                LinkedHashMap d = (LinkedHashMap) c.get("name");
                LinkedHashMap e = (LinkedHashMap) d.get("canonical");
                String acidNameDe = e.entrySet().toArray()[1].toString();
                assertEquals("Unicode escaped sequence must be decoded.",
                        ":de=AcetylsalicylsÃ¤ure", acidNameDe);
            }
            assertTrue(oid > 10000);
            counter++;
        }
        assertEquals(4, counter);
        assertEquals(0, CodeBean.counter);
    }

    private class CustomConstructor extends SafeConstructor {
        public CustomConstructor() {
            // define tags which begin with !org.yaml.
            String prefix = "!de.oddb.org,2007/ODDB";
            this.yamlMultiConstructors.put(prefix, new ConstructYamlMap());
        }
    }

    @SuppressWarnings("unchecked")
    public void testJavaBeanWithTypeDescription() {
        Constructor c = new CustomBeanConstructor();
        TypeDescription descr = new TypeDescription(CodeBean.class, new Tag(
                "!de.oddb.org,2007/ODDB::Util::Code"));
        c.addTypeDescription(descr);
        Yaml yaml = new Yaml(c);
        String input = Util.getLocalResource("issues/issue56-1.yaml");
        int counter = 0;
        for (Object obj : yaml.loadAll(input)) {
            // System.out.println(obj);
            Map<String, Object> map = (Map<String, Object>) obj;
            Integer oid = (Integer) map.get("oid");
            assertTrue(oid > 10000);
            counter++;
        }
        assertEquals(4, counter);
        assertEquals(55, CodeBean.counter);
    }

    @SuppressWarnings("unchecked")
    public void testJavaBean() {
        Constructor c = new CustomBeanConstructor();
        Yaml yaml = new Yaml(c);
        String input = Util.getLocalResource("issues/issue56-1.yaml");
        int counter = 0;
        for (Object obj : yaml.loadAll(input)) {
            // System.out.println(obj);
            Map<String, Object> map = (Map<String, Object>) obj;
            Integer oid = (Integer) map.get("oid");
            assertTrue(oid > 10000);
            counter++;
        }
        assertEquals(4, counter);
        assertEquals(55, CodeBean.counter);
    }

    private class CustomBeanConstructor extends Constructor {
        public CustomBeanConstructor() {
            // define tags which begin with !org.yaml.
            String prefix = "!de.oddb.org,2007/ODDB";
            this.yamlMultiConstructors.put(prefix, new ConstructYamlMap());
        }

        protected Construct getConstructor(Node node) {
            if (node.getTag().equals(new Tag("!de.oddb.org,2007/ODDB::Util::Code"))) {
                node.setUseClassConstructor(true);
                node.setType(CodeBean.class);
            }
            return super.getConstructor(node);
        }
    }

    @Override
    protected void setUp() {
        CodeBean.counter = 0;
    }
}
