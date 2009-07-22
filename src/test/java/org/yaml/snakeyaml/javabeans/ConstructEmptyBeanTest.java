/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.javabeans;

import java.io.IOException;
import java.io.Serializable;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Yaml;

public class ConstructEmptyBeanTest extends TestCase {
    /**
     * standard Yaml
     */
    public void testEmptyBean() throws IOException {
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
    public void testEmptyBean1() throws IOException {
        JavaBeanLoader<EmptyBean> beanLoader = new JavaBeanLoader<EmptyBean>(EmptyBean.class);
        EmptyBean bean = beanLoader
                .load("!!org.yaml.snakeyaml.javabeans.ConstructEmptyBeanTest$EmptyBean {}");
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * global tag is ignored
     */
    public void testEmptyBean2() throws IOException {
        JavaBeanLoader<EmptyBean> beanLoader = new JavaBeanLoader<EmptyBean>(EmptyBean.class);
        EmptyBean bean = beanLoader.load("!!Bla-bla-bla {}");
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * no tag
     */
    public void testEmptyBean3() throws IOException {
        JavaBeanLoader<EmptyBean> beanLoader = new JavaBeanLoader<EmptyBean>(EmptyBean.class);
        EmptyBean bean = beanLoader.load("{   }");
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * empty document
     */
    public void testEmptyBean4() throws IOException {
        JavaBeanLoader<EmptyBean> beanLoader = new JavaBeanLoader<EmptyBean>(EmptyBean.class);
        EmptyBean bean = beanLoader.load("");
        assertNull(bean);
    }

    /**
     * local tag is ignored
     */
    public void testEmptyBean5() throws IOException {
        JavaBeanLoader<EmptyBean> beanLoader = new JavaBeanLoader<EmptyBean>(EmptyBean.class);
        EmptyBean bean = beanLoader.load("!Bla-bla-bla {}");
        assertNotNull(bean);
        assertNull(bean.getFirstName());
        assertEquals(5, bean.getHatSize());
    }

    /**
     * invalid document
     */
    public void testEmptyBean6() throws IOException {
        JavaBeanLoader<EmptyBean> beanLoader = new JavaBeanLoader<EmptyBean>(EmptyBean.class);
        try {
            beanLoader.load("{");
            fail("Invalid document provided.");
        } catch (Exception e) {
            assertEquals(
                    "while parsing a flow node; expected the node content, but found <stream end>",
                    e.getMessage());
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
