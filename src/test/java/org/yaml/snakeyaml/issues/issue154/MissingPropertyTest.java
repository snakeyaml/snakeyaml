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
package org.yaml.snakeyaml.issues.issue154;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

public class MissingPropertyTest extends TestCase {

    private Yaml yaml;

    public void setUp() {
        yaml = new Yaml();
    }

    /**
     * A normal scalar property should work fine.
     */
    public void testPublicField() throws Exception {
        String doc = "hello: 5";
        TestBean bean = yaml.loadAs(doc, TestBean.class);
        assertNotNull(bean);
        assertEquals(5, bean.hello);
    }

    /**
     * By default, unknown fields should throw a YAMLException.
     */
    public void testUnknownField() throws Exception {
        try {
            String doc = "goodbye: 10";
            yaml.loadAs(doc, TestBean.class);
        } catch (YAMLException e) {
            assertTrue(e.getMessage().contains("Cannot create property=goodbye"));
        }
    }

    /**
     * A new method setSkipMissingProperties(boolean) was added to configure
     * whether missing properties should throw a YAMLException (the default) or
     * simply show a warning. The default is false.
     */
    public void testSkipMissingProperties() throws Exception {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        yaml = new Yaml(new Constructor(), representer);
        String doc = "goodbye: 10\nhello: 5\nfizz: [1]";
        TestBean bean = yaml.loadAs(doc, TestBean.class);
        assertNotNull(bean);
        assertEquals(5, bean.hello);
    }

    /**
     * The default for setSkipMissingProperties(boolean) is false; this just
     * ensures it works if set manually.
     */
    public void testNoSkipMissingProperties() throws Exception {
        try {
            Representer representer = new Representer();
            representer.getPropertyUtils().setSkipMissingProperties(false);
            yaml = new Yaml(new Constructor(), representer);
            String doc = "goodbye: 10";
            yaml.loadAs(doc, TestBean.class);
        } catch (YAMLException e) {
            assertTrue(e.getMessage().contains("Cannot create property=goodbye"));
        }
    }
}
