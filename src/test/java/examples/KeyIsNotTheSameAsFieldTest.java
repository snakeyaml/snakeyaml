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
package examples;

import static org.junit.Assert.*;

import org.junit.Test;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

public class KeyIsNotTheSameAsFieldTest {

    public static class Param {
        private String name;
        private String inputPart;
        private String more;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInputPart() {
            return inputPart;
        }

        public void setInputPart(String inputPart) {
            this.inputPart = inputPart;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }
    }

    @Test
    public void loadFromStr() {
        Param p = createYaml().loadAs(
                "name: \"Test\"\ninput_part: \"abc\"\ndefault: \"some value\"", Param.class);

        assertEquals("Test", p.getName());
        assertEquals("abc", p.getInputPart());
        assertEquals("some value", p.getMore());
    }

    @Test
    public void dumpNload() {
        Param realParam = new Param();
        realParam.setName("Test");
        realParam.setInputPart("abc");
        realParam.setMore("some value");

        String yamlStr = createYaml().dump(realParam);
        Param loadedParam = createYaml().loadAs(yamlStr, Param.class);

        assertEquals(realParam.getName(), loadedParam.getName());
        assertEquals(realParam.getInputPart(), loadedParam.getInputPart());
        assertEquals(realParam.getMore(), loadedParam.getMore());
    }

    private Yaml createYaml() {
        TypeDescription paramDesc = new TypeDescription(Param.class);
        paramDesc.substituteProperty("input_part", String.class, "getInputPart", "setInputPart");
        paramDesc.substituteProperty("default", String.class, "getMore", "setMore");

        /*
         * Need to exclude real properties. Otherwise we get them in dump in
         * addition to "generated" ones:
         *
         * {input_part: ?1?, default: ?2?, inputPart: ?1?, more: ?2?, name: ???}
         *
         * not just
         *
         * {input_part: ?1?, default: ?2?, name: ???}
         */
        paramDesc.setExcludes("inputPart", "more");

        Yaml yaml = new Yaml();
        yaml.addTypeDescription(paramDesc);
        return yaml;
    }
}
