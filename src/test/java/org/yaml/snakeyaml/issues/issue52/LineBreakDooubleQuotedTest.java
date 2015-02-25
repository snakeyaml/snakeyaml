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
package org.yaml.snakeyaml.issues.issue52;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

/**
 * @see <a
 *      href="http://code.google.com/p/snakeyaml/issues/detail?id=52">Issue</a>
 */
public class LineBreakDooubleQuotedTest extends TestCase {
    public void testDoubleQuotedStyle() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED);
        options.setWidth(20);
        options.setIndent(4);
        Yaml yaml = new Yaml(options);
        String etalon = "12345678901234567890\n\n123  456";
        String output = yaml.dump(etalon);
        // System.out.println(output);
        assertEquals("\"12345678901234567890\\n\\\n    \\n123  456\"\n", output);
        String parsed = (String) yaml.load(output);
        assertEquals(etalon, parsed);
    }

    public void testDoubleQuotedStyleNoLineSplit() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED);
        options.setWidth(20);
        options.setSplitLines(false);
        options.setIndent(4);
        Yaml yaml = new Yaml(options);
        String etalon = "12345678901234567890\n\n123  456";
        String output = yaml.dump(etalon);
        // System.out.println(output);
        assertEquals("\"12345678901234567890\\n\\n123  456\"\n", output);
        String parsed = (String) yaml.load(output);
        assertEquals(etalon, parsed);
    }
}
