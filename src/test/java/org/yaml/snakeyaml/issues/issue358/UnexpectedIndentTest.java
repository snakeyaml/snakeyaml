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
package org.yaml.snakeyaml.issues.issue358;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UnexpectedIndentTest {

    @Test
    public void testIndicatorIndentMuchSmaller() {
        check(4, 6); // standard
        check(4, 5); // ugly, but acceptable, because the YAML will be valid
        try {
            check(4, 4);
            fail("Invalid indent may cause invalid YAML");
        } catch (YAMLException e) {
            assertEquals("Indicator indent must be smaller then indent.", e.getMessage());
        }
        try {
            check(6, 4);
            fail("Invalid indent may cause invalid YAML");
        } catch (YAMLException e) {
            assertEquals("Indicator indent must be smaller then indent.", e.getMessage());
        }
    }

    private void check(int indicatorIndent, int indent) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndicatorIndent(indicatorIndent);
        options.setIndent(indent);
        Map<String, Object> map = create();
        String dumped = new Yaml(options).dump(map);
        //System.out.println(dumped);
        Map<String, Object> parsed = (Map<String, Object>) new Yaml().load(dumped);
        assertEquals(map, parsed);
    }

    private Map<String, Object> create() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> member1 = new HashMap<String, String>();
        member1.put("db_1", "ds");
        member1.put("name", "asd");
        list.add(member1);
        Map<String, String> member2 = new HashMap<String, String>();
        member2.put("db_2", "daas");
        member2.put("name", "adas");
        list.add(member2);
        map.put("some", list);
        return map;
    }
}
