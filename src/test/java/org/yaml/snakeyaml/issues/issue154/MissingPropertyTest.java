/**
 * Copyright (c) 2008-2012, http://www.snakeyaml.org
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

public class MissingPropertyTest {

    private Yaml yaml;

    @Before
    public void init() {
        yaml = new Yaml();
    }

    /**
     * A normal scalar property should work fine.
     */
    @Test
    public void publicField() throws Exception {
        String doc = "hello: 5";
        TestBean bean = yaml.loadAs(doc, TestBean.class);
        assertNotNull(bean);
        assertEquals(5, bean.hello);
    }

    /**
     * By default, unknown fields should throw a YAMLException.
     */
    @Test(expected = YAMLException.class)
    public void unknownField() throws Exception {
        String doc = "goodbye: 10";
        yaml.loadAs(doc, TestBean.class);
    }

    /**
     * A new method setSkipMissingProperties(boolean) was added to configure
     * whether missing properties should throw a YAMLException (the default) or
     * simply show a warning. The default is false.
     */
    @Test
    public void skipMissingProperties() throws Exception {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        yaml = new Yaml(new Constructor(), representer);
        String doc = "goodbye: 10\nhello: 5";
        TestBean bean = yaml.loadAs(doc, TestBean.class);
        assertNotNull(bean);
        assertEquals(5, bean.hello);
    }

    /**
     * The default for setSkipMissingProperties(boolean) is false; this just
     * ensures it works if set manually.
     */
    @Test(expected = YAMLException.class)
    public void noSkipMissingProperties() throws Exception {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(false);
        yaml = new Yaml(new Constructor(), representer);
        String doc = "goodbye: 10";
        yaml.loadAs(doc, TestBean.class);
    }
}
