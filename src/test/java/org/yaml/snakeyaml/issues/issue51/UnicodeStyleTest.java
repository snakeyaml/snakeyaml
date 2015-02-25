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
package org.yaml.snakeyaml.issues.issue51;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.Yaml;

/**
 * @see <a
 *      href="http://code.google.com/p/snakeyaml/issues/detail?id=51">Issue</a>
 */
public class UnicodeStyleTest extends TestCase {
    public void testFoldedStyle() {
        Yaml yaml = new Yaml();
        String output = yaml.dump("í");
        // System.out.println(output);
        assertEquals("í\n", output);
    }

    public void testDoubleQuotedStyle() {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(ScalarStyle.DOUBLE_QUOTED);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump("í");
        // System.out.println(output);
        assertEquals("\"í\"\n", output);
    }
}
