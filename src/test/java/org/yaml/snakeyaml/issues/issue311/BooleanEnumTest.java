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
package org.yaml.snakeyaml.issues.issue311;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import static org.junit.Assert.assertEquals;

public class BooleanEnumTest {

    @Test
    public void laodFromString() {

        BeanWithEnum bean = new BeanWithEnum(true, "10", BooleanEnum.TRUE);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAs(bean, Tag.MAP, DumperOptions.FlowStyle.FLOW);
        assertEquals("{boolField: true, enumField: 'TRUE', name: '10'}\n", output);
        BeanWithEnum parsed = yaml.loadAs(output, BeanWithEnum.class);
        assertEquals(bean.getEnumField(), parsed.getEnumField());
        assertEquals(bean.getName(), parsed.getName());
    }
}
