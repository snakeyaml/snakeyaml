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
package org.yaml.snakeyaml.issues.issue47;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class ReadOnlyPropertiesTest extends TestCase {
    public void testBean1() {
        IncompleteBean bean = new IncompleteBean();
        bean.setName("lunch");
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals("name: lunch\n", output);
        //
        Yaml loader = new Yaml();
        IncompleteBean parsed = loader.loadAs(output, IncompleteBean.class);
        assertEquals(bean.getName(), parsed.getName());
    }

    public void testBean2() {
        IncompleteBean bean = new IncompleteBean();
        bean.setName("lunch");
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals("id: 10\nname: lunch\n", output);
        //
        Yaml loader = new Yaml();
        try {
            loader.loadAs(output, IncompleteBean.class);
            fail("Setter is missing.");
        } catch (YAMLException e) {
            String message = e.getMessage();
            assertTrue(
                    message,
                    message.contains("Unable to find property 'id' on class: org.yaml.snakeyaml.issues.issue47.IncompleteBean"));
        }
    }
}
