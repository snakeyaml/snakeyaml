/**
 * Copyright (c) 2008-2010 Andrey Somov
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
package org.yaml.snakeyaml.issues.issue50;

import java.util.Properties;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * test issue 50.
 */
public class SnakeyamlTest extends TestCase {
    public static interface SomeBean {
        String getAttribute1();

        String getAttribute2();
    }

    /* public */static abstract class BaseSomeBean implements SomeBean {
        private String attribute1;

        public String getAttribute1() {
            return attribute1;
        }

        public void setAttribute1(String attribute1) {
            this.attribute1 = attribute1;
        }
    }

    public static final class SomeBeanImpl extends BaseSomeBean {
        private String attribute2;

        public SomeBeanImpl(final String attribute1, final String attribute2) {
            setAttribute1(attribute1);
            setAttribute2(attribute2);
        }

        public String getAttribute2() {
            return attribute2;
        }

        public void setAttribute2(String attribute2) {
            this.attribute2 = attribute2;
        }

        @Override
        public String toString() {
            return "SomeBeanImpl";
        }
    }

    public void testIntrospector() throws Exception {
        SomeBean someBean = new SomeBeanImpl("value1", "value2");
        Yaml dumper = new Yaml();
        try {
            String output = dumper.dump(someBean);
            // System.out.println(output);
            assertEquals(
                    "!!org.yaml.snakeyaml.issues.issue50.SnakeyamlTest$SomeBeanImpl {attribute1: value1,\n  attribute2: value2}\n",
                    output);
        } catch (Exception e) {
            System.out
                    .println("Unexpected result. Check issue 50. http://code.google.com/p/snakeyaml/issues/detail?id=50");
            Properties props = System.getProperties();
            for (Object key : new String[] { "java.runtime.name", "java.vm.version",
                    "java.vm.vendor", "java.vm.name", "java.runtime.version", "os.version",
                    "java.specification.version", "java.version" }) {
                System.out.println(key.toString() + ": " + props.getProperty(key.toString()));
            }
        }
    }
}
