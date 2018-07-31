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
package org.yaml.snakeyaml.issues.issue409;

import junit.framework.TestCase;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

public class DumpEnumAsJavabeanPropertyTest extends TestCase {

    public static class Bean {

        public MyEnum myEnum = MyEnum.B;

    }

    public enum MyEnum {
        A, B
        //A{}, B{} // issue 409
    }

    public void testDumpEnum() {
        Yaml yaml = new Yaml();
        String text = yaml.dumpAs(new Bean(), Tag.MAP, DumperOptions.FlowStyle.AUTO);
        assertEquals("{myEnum: B}\n", text);
//        assertEquals("{myEnum: !!org.yaml.snakeyaml.issues.issue409.DumpEnumAsJavabeanPropertyTest$MyEnum$2 'B'}\n", text);
        Bean actual = yaml.loadAs(text, Bean.class);
        assertEquals(MyEnum.B, actual.myEnum);
        assertEquals("{myEnum: B}\n", text);
    }
}
