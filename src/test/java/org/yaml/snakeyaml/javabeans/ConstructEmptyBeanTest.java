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
package org.yaml.snakeyaml.javabeans;

import java.io.Serializable;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class ConstructEmptyBeanTest extends TestCase {
    /**
     * standard Yaml
     */
    public void testEmptyBean() {
        Yaml yaml = new Yaml();
        EmptyBean bean = (EmptyBean) yaml
                .load("!!org.yaml.snakeyaml.javabeans.ConstructEmptyBeanTest$EmptyBean {}");
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * global tag is correct (but ignored)
     */
    public void testEmptyBean1() {
        Yaml beanLoader = new Yaml();
        EmptyBean bean = beanLoader.loadAs(
                "!!org.yaml.snakeyaml.javabeans.ConstructEmptyBeanTest$EmptyBean {}",
                EmptyBean.class);
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * global tag is ignored
     */
    public void testEmptyBean2() {
        Yaml beanLoader = new Yaml();
        EmptyBean bean = beanLoader.loadAs("!!Bla-bla-bla {}", EmptyBean.class);
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * no tag
     */
    public void testEmptyBean3() {
        Yaml beanLoader = new Yaml();
        EmptyBean bean = beanLoader.loadAs("{   }", EmptyBean.class);
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * empty document
     */
    public void testEmptyBean4() {
        Yaml beanLoader = new Yaml();
        EmptyBean bean = beanLoader.loadAs("", EmptyBean.class);
        assertNull(bean);
    }

    /**
     * local tag is ignored
     */
    public void testEmptyBean5() {
        Yaml beanLoader = new Yaml();
        EmptyBean bean = beanLoader.loadAs("!Bla-bla-bla {}", EmptyBean.class);
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * invalid document
     */
    public void testEmptyBean6() {
        Yaml beanLoader = new Yaml();
        try {
            beanLoader.loadAs("{", EmptyBean.class);
            fail("Invalid document provided.");
        } catch (Exception e) {
            assertEquals("while parsing a flow node\n" + " in 'string', line 1, column 2:\n"
                    + "    {\n" + "     ^\n" + "expected the node content, but found StreamEnd\n"
                    + " in 'string', line 1, column 2:\n" + "    {\n" + "     ^\n", e.getMessage());
        }
    }

    public static class EmptyBean implements Serializable {
        private static final long serialVersionUID = -8001155967276657180L;
        private String firstName;
        private int hatSize = 5;

        public EmptyBean() {
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public int getHatSize() {
            return hatSize;
        }

        public void setHatSize(int hatSize) {
            this.hatSize = hatSize;
        }
    }
}
