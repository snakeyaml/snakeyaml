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

import junit.framework.TestCase;

import org.junit.Test;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

public class PropertyUtilsSharingTest extends TestCase {

    public void testYamlDefaults() {
        Yaml yaml1 = new Yaml();
        assertSame(yaml1.constructor.getPropertyUtils(), yaml1.representer.getPropertyUtils());

        Yaml yaml2 = new Yaml(new Constructor());
        assertSame(yaml2.constructor.getPropertyUtils(), yaml2.representer.getPropertyUtils());

        Yaml yaml3 = new Yaml(new Representer());
        assertSame(yaml3.constructor.getPropertyUtils(), yaml3.representer.getPropertyUtils());
    }

    public void testYamlConstructorWithPropertyUtils() {
        Constructor constructor1 = new Constructor();
        PropertyUtils pu = new PropertyUtils();
        constructor1.setPropertyUtils(pu);
        Yaml yaml = new Yaml(constructor1);
        assertSame(pu, yaml.constructor.getPropertyUtils());
        assertSame(pu, yaml.representer.getPropertyUtils());
    }

    public void testYamlRepresenterWithPropertyUtils() {
        Representer representer2 = new Representer();
        PropertyUtils pu = new PropertyUtils();
        representer2.setPropertyUtils(pu);
        Yaml yaml = new Yaml(representer2);
        assertSame(pu, yaml.constructor.getPropertyUtils());
        assertSame(pu, yaml.representer.getPropertyUtils());
    }

    @Test
    public void testYamlConstructorANDRepresenterWithPropertyUtils() {
        Constructor constructor = new Constructor();
        PropertyUtils pu_c = new PropertyUtils();
        constructor.setPropertyUtils(pu_c);
        Representer representer = new Representer();
        PropertyUtils pu_r = new PropertyUtils();
        representer.setPropertyUtils(pu_r);
        Yaml yaml = new Yaml(constructor, representer);
        assertSame(pu_c, yaml.constructor.getPropertyUtils());
        assertSame(pu_r, yaml.representer.getPropertyUtils());
    }
}
