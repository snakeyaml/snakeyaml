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
package org.yaml.snakeyaml.issues.issue176;

import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.representer.Representer;

public class SingleQuoteTest extends TestCase {

    public void testNoSingleQuoteForBlockStyle() throws Exception {
        checkQuotes(true, "cows:\n    steak:cow: '11'");
    }

    public void testSingleQuoteForFlowStyle() throws Exception {
        checkQuotes(false, "cows: {'steak:cow': '11'}");
    }

    private void checkQuotes(boolean isBlock, String expectation) {
        DumperOptions options = new DumperOptions();
        options.setIndent(4);
        if (isBlock) {
            options.setDefaultFlowStyle(FlowStyle.BLOCK);
        }
        Representer representer = new Representer();

        Yaml yaml = new Yaml(new SafeConstructor(), representer, options);

        LinkedHashMap<String, Object> lvl1 = new LinkedHashMap<String, Object>();
        lvl1.put("steak:cow", "11");
        LinkedHashMap<String, Object> root = new LinkedHashMap<String, Object>();
        root.put("cows", lvl1);
        String output = yaml.dump(root);
        assertEquals(expectation + "\n", output);

        // parse the value back
        @SuppressWarnings("unchecked")
        Map<String, Object> cows = (Map<String, Object>) yaml.load(output);
        @SuppressWarnings("unchecked")
        Map<String, String> cow = (Map<String, String>) cows.get("cows");
        assertEquals("11", cow.get("steak:cow"));
    }
}
