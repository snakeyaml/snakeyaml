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
package org.yaml.snakeyaml.issues.issue409;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import junit.framework.TestCase;

public class DumpEnumAsJavabeanPropertyTest extends TestCase {
    public static class Bean {
        public ExtendedEnum myEnum = ExtendedEnum.B;
    }

    public enum ExtendedEnum {
        A {
            public String toGreek() {
                return "alpha";
            }
        },
        B {
            public String toGreek() {
                return "beta";
            }
        } // issue 409
    }

    public void testDumpExtendedEnum() {
        Yaml yaml = new Yaml();
        String text = yaml.dumpAs(new Bean(), Tag.MAP, DumperOptions.FlowStyle.AUTO);
        assertEquals("{myEnum: B}\n", text);
        Bean actual = yaml.loadAs(text, Bean.class);
        assertEquals(ExtendedEnum.B, actual.myEnum);
    }
}
