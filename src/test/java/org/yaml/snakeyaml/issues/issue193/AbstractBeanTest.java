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
package org.yaml.snakeyaml.issues.issue193;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

public class AbstractBeanTest extends TestCase {

    public void testErrorMessage() throws Exception {

        BeanA1 b = new BeanA1();
        b.setId(2l);
        b.setName("name1");

        Constructor c = new Constructor();
        Representer r = new Representer();

        PropertyUtils pu = new PropertyUtils();
        c.setPropertyUtils(pu);
        r.setPropertyUtils(pu);

        pu.getProperties(BeanA1.class, BeanAccess.FIELD);

        Yaml yaml = new Yaml(c, r);
        // yaml.setBeanAccess(BeanAccess.FIELD);
        String dump = yaml.dump(b);
        BeanA1 b2 = (BeanA1) yaml.load(dump);
        assertEquals(b.getId(), b2.getId());
        assertEquals(b.getName(), b2.getName());
    }
}
