/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue348;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.issues.issue348.model.Bar;
import org.yaml.snakeyaml.issues.issue348.model.Baz;
import org.yaml.snakeyaml.issues.issue348.model.Foo;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class MultiLevelImmutableTest {

    @Test
    public void testUnexpectedRecursive() {
        Yaml yaml = new Yaml();
        String data = Util.getLocalResource("issues/issue348.yaml");
        Map<?, ?> loadedMap = yaml.loadAs(data, Map.class);

        for (Map.Entry<?, ?> entry : loadedMap.entrySet()) {
            assertThat(entry.getValue(), instanceOf(List.class));
        }

        Object foo = ((List) loadedMap.get("foo")).get(0);
        Object bar = ((List) loadedMap.get("bar")).get(0);
        Object baz = ((List) loadedMap.get("baz")).get(0);
        assertThat(foo, instanceOf(Foo.class));
        assertThat(bar, instanceOf(Bar.class));
        assertThat(baz, instanceOf(Baz.class));

        assertEquals(foo, ((Bar) bar).getFoo());
        assertEquals(bar, ((Baz) baz).getBar());
        assertEquals("foo", ((Foo) foo).getFoo());
    }
}
