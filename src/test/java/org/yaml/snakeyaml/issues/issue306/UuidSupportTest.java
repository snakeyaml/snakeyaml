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
package org.yaml.snakeyaml.issues.issue306;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class UuidSupportTest {

    //TODO @Test
    public void loadFromString() {
        String str = UUID.randomUUID().toString();
        Yaml yaml = new Yaml();
        UUID uuid = (UUID) yaml.load(str);
        assertEquals(str, uuid.toString());
    }

    @Test
    public void loadFromBean() {
        String input = Util.getLocalResource("issues/issue306-1.yaml");
        Yaml yaml = new Yaml();
        BeanWithId bean = (BeanWithId) yaml.loadAs(input, BeanWithId.class);
        assertEquals("7f511847-781a-45df-9c8d-1e32e028b9b3", bean.getId().toString());
    }

    @Test
    public void dumpUuid() {
        UUID uuid = UUID.randomUUID();
        Yaml yaml = new Yaml();
        String output = yaml.dump(uuid);
        assertEquals("!!java.util.UUID '" + uuid.toString() + "'\n", output);
    }

    @Test
    public void dumpBean() {
        BeanWithId bean = new BeanWithId();
        bean.setValue(3);
        UUID uuid = UUID.fromString("ac4877be-0c31-4458-a86e-0272efe1aaa8");
        bean.setId(uuid);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAs(bean, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
        String expected = Util.getLocalResource("issues/issue306-2.yaml");
        assertEquals(expected, output);
    }
}
