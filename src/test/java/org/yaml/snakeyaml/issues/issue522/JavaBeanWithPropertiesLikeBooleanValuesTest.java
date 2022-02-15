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
package org.yaml.snakeyaml.issues.issue522;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class JavaBeanWithPropertiesLikeBooleanValuesTest {

    public static class Workflow {
        public String on;
        public String off;
        public String y;
        public String n;
        public String yes;
        public String no;
    }

    @Test
    public void deserialize() {
        Yaml yaml = new Yaml();

        String strYaml =
            "on: This is on\n" +
            "off: This is off\n" +
            "y: This is y\n" +
            "n: This is n\n" +
            "yes: This is yes\n" +
            "no: This is no\n";

        Workflow wf = yaml.loadAs(strYaml, Workflow.class);

        assertEquals(wf.on, "This is on");
        assertEquals(wf.off, "This is off");
        assertEquals(wf.y, "This is y");
        assertEquals(wf.n, "This is n");
        assertEquals(wf.yes, "This is yes");
        assertEquals(wf.no, "This is no");

    }

}
