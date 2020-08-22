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
package org.yaml.snakeyaml.issues.issue416;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IndentWithIndicatorTest extends TestCase {
    public void testIndentWithIndicator1() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndentWithIndicator(true);
        options.setIndent(2);
        options.setIndicatorIndent(1);

        Yaml yaml = new Yaml(options);
        String output = yaml.dump(createData());

        String doc = Util.getLocalResource("issues/issue416-1.yaml");

        assertEquals(doc, output);
    }

    public void testIndentWithIndicator2() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndentWithIndicator(true);
        options.setIndent(2);
        options.setIndicatorIndent(2);

        Yaml yaml = new Yaml(options);
        String output = yaml.dump(createData());

        String doc = Util.getLocalResource("issues/issue416-2.yaml");

        assertEquals(doc, output);
    }

    public void testIndentWithIndicator3() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndentWithIndicator(false);
        options.setIndent(4);
        options.setIndicatorIndent(2);

        Yaml yaml = new Yaml(options);
        String output = yaml.dump(createData());

        String doc = Util.getLocalResource("issues/issue416_3.yml");

        assertEquals(doc, output);
    }

    private Map<String, Object> createData() {
        Map<String, String> fred = new LinkedHashMap<>();
        fred.put("name", "Fred");
        fred.put("role", "creator");

        Map<String, String> john = new LinkedHashMap<>();
        john.put("name", "John");
        john.put("role", "committer");

        List<Map<String, String>> developers = new ArrayList<>();
        developers.add(fred);
        developers.add(john);

        Map<String, Object> company = new LinkedHashMap<>();
        company.put("developers", developers);
        company.put("name", "Yet Another Company");
        company.put("location", "Maastricht");

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("company", company);

        return data;
    }
}
