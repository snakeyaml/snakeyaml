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
package org.yaml.snakeyaml.issues.issue375;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.ConstructorException;

public class EmptyDocumentTest {

    private static final String EMPTY_YAML =
            "%YAML 1.2\n"
                    + "---\n";

    private static final String EMPTY_YAML_WITH_COMMENT =
            EMPTY_YAML + "#Use all default values\n...";

    @Test
    public void emptyYamlTestAsString() {
        Yaml yaml = new Yaml();
        String str = yaml.loadAs(EMPTY_YAML_WITH_COMMENT, String.class);
        Assert.assertEquals("", str);
    }

    @Test(expected = ConstructorException.class)
    public void almostEmptyYamlTestAsObject() {
        Yaml yaml = new Yaml();
        yaml.loadAs(EMPTY_YAML_WITH_COMMENT, TestObject.class);
    }

    @Test(expected = ConstructorException.class)
    public void emptyYamlTestAsObject() {
        Yaml yaml = new Yaml();
        yaml.loadAs(EMPTY_YAML, TestObject.class);
    }

    public static class TestObject {
        private int attribute1;
        private boolean attribute2;

        public int getAttribute1() {
            return attribute1;
        }

        public void setAttribute1(int attribute1) {
            this.attribute1 = attribute1;
        }

        public boolean isAttribute2() {
            return attribute2;
        }

        public void setAttribute2(boolean attribute2) {
            this.attribute2 = attribute2;
        }
    }
}