/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class RepresenterTest extends TestCase {

    public void testRepresenter() {
        MyBean bean = new MyBean();
        bean.setName("Gnome");
        bean.setValid(true);
        bean.setPrimitive(true);
        Yaml yaml = new Yaml();
        assertEquals(
                "!!org.yaml.snakeyaml.representer.RepresenterTest$MyBean {name: Gnome, primitive: true}\n",
                yaml.dump(bean));
    }

    public static class MyBean {
        private String name;
        private Boolean valid;
        private boolean primitive;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean isValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }

        public boolean isPrimitive() {
            return primitive;
        }

        public void setPrimitive(boolean primitive) {
            this.primitive = primitive;
        }
    }

    public void testRepresenterNoConstructorAvailable() {
        MyBean2 bean = new MyBean2("Gnome", true);
        Yaml yaml = new Yaml();
        assertEquals("!!org.yaml.snakeyaml.representer.RepresenterTest$MyBean2 {valid: true}\n",
                yaml.dump(bean));
    }

    public static class MyBean2 {
        private String name;
        private Boolean valid;

        public MyBean2(String name, Boolean valid) {
            this();
            this.name = name;
            this.valid = valid;
        }

        private MyBean2() {
            super();
        }

        private String getName() {
            return name;
        }

        public Boolean getValid() {
            return valid;
        }

        @Override
        public String toString() {
            return getName() + " " + getValid();
        }
    }

    public void testRepresenterGetterWithException() {
        MyBean3 bean = new MyBean3("Gnome", true);
        Yaml yaml = new Yaml();
        try {
            yaml.dump(bean);
            fail("Exception must be reported");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public static class MyBean3 {
        private String name;
        private Boolean valid;

        public MyBean3(String name, Boolean valid) {
            this.name = name;
            this.valid = valid;
        }

        public String getName() {
            throw new UnsupportedOperationException("Test.");
        }

        public Boolean isValid() {
            return valid;
        }

        @Override
        public String toString() {
            return name + " " + isValid();
        }
    }

    public void testRepresenterAddNull() {
        Representer representer = new Representer();
        try {
            representer.addTypeDescription(null);
            fail("Representer must be provided.");
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}
