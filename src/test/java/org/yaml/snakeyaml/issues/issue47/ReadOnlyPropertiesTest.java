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

package org.yaml.snakeyaml.issues.issue47;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class ReadOnlyPropertiesTest extends TestCase {
    public void testBean1() {
        IncompleteBean bean = new IncompleteBean();
        bean.setName("lunch");
        JavaBeanDumper yaml = new JavaBeanDumper();
        String output = yaml.dump(bean);
        // System.out.println(output);
        assertEquals("name: lunch\n", output);
        //
        JavaBeanLoader<IncompleteBean> loader = new JavaBeanLoader<IncompleteBean>(
                IncompleteBean.class);
        IncompleteBean parsed = loader.load(output);
        assertEquals(bean.getName(), parsed.getName());
    }

    public void testBean2() {
        IncompleteBean bean = new IncompleteBean();
        bean.setName("lunch");
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot(Tag.MAP);
        options.setAllowReadOnlyProperties(true);
        JavaBeanDumper yaml = new JavaBeanDumper(new Representer(), options);
        String output = yaml.dump(bean);
        // System.out.println(output);
        assertEquals("{id: 10, name: lunch}\n", output);
        //
        JavaBeanLoader<IncompleteBean> loader = new JavaBeanLoader<IncompleteBean>(
                IncompleteBean.class);
        try {
            loader.load(output);
            fail("Setter is missing.");
        } catch (YAMLException e) {
            String message = e.getMessage();
            assertTrue(
                    message,
                    message.contains("Unable to find property 'id' on class: org.yaml.snakeyaml.issues.issue47.IncompleteBean"));
        }
    }
}
