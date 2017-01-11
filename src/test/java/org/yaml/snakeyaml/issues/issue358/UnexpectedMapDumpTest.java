/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UnexpectedMapDumpTest {

    @Test
    public void testBlockStyle() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndicatorIndent(2);
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
        String dumped = new Yaml(options).dump(map);
        //System.out.println(dumped);
        try {
            new Yaml().load(dumped);
            fail("Fix issue 358");
        } catch (Exception e) {
            assertTrue("TODO", e.getMessage().contains("expected the node content"));
        }
    }
}
