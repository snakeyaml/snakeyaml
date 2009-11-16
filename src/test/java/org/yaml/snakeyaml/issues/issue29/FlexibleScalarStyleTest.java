/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package org.yaml.snakeyaml.issues.issue29;

import java.io.IOException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=29
 */
public class FlexibleScalarStyleTest extends TestCase {
    public void testLong() throws IOException {
        Yaml yaml = new Yaml(new MyOptions());
        String result = yaml
                .dump("qqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqq "
                        + "qqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqq "
                        + "qqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqqqq\n "
                        + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqq qqqqqqqqqqqqqqqqqqqqqqqqqqq\n");
        // System.out.println(result);
        assertTrue(result.startsWith(">\n"));
    }

    public void testShort() throws IOException {
        Yaml yaml = new Yaml(new MyOptions());
        String result = yaml.dump("qqqqqqqqqqqqqqqqqq");
        // System.out.println(result);
        assertEquals("qqqqqqqqqqqqqqqqqq\n", result);
    }

    private class MyOptions extends DumperOptions {
        @Override
        public ScalarStyle chooseScalarStyle(ScalarAnalysis analysis, ScalarStyle style) {
            if (analysis.allowBlock && analysis.scalar.length() > 80) {
                return ScalarStyle.FOLDED;
            } else {
                return super.chooseScalarStyle(analysis, style);
            }
        }
    }
}
