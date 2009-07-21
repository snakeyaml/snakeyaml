package org.yaml.snakeyaml;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class JavaBeanDumperTest extends TestCase {

    public void testDumpObjectWriter() {
        DumpBean bean = new DumpBean();
        bean.setName("Name1");
        bean.setNumber(3);
        JavaBeanDumper dumper = new JavaBeanDumper();
        String output = dumper.dump(bean);
        assertEquals("name: Name1\nnumber: 3\n", output);
        assertEquals(FlowStyle.BLOCK, dumper.getFlowStyle());
        assertFalse(dumper.isUseGlobalTag());
        //
        dumper.setFlowStyle(FlowStyle.AUTO);
        output = dumper.dump(bean);
        assertEquals("{name: Name1, number: 3}\n", output);
        //
        dumper.setUseGlobalTag(true);
        output = dumper.dump(bean);
        assertEquals("!!org.yaml.snakeyaml.JavaBeanDumperTest$DumpBean {name: Name1, number: 3}\n",
                output);
    }

    public void testDumpObject() {
        DumpBean bean = new DumpBean();
        bean.setName("Name2");
        bean.setNumber(4);
        JavaBeanDumper dumper = new JavaBeanDumper();
        StringWriter buffer = new StringWriter();
        dumper.dump(bean, buffer);
        assertEquals("name: Name2\nnumber: 4\n", buffer.toString());

    }

    public static class DumpBean {
        private String name;
        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
