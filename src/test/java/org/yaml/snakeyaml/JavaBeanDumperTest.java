/**
 * Copyright (c) 2008-2014, http://www.snakeyaml.org
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
package org.yaml.snakeyaml;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.representer.Representer;

@SuppressWarnings("deprecation")
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

    public void testDumpObject2() {
        DumpBean bean = new DumpBean();
        bean.setName("Name2");
        bean.setNumber(4);
        JavaBeanDumper dumper = new JavaBeanDumper();
        StringWriter buffer = new StringWriter();
        dumper.dump(bean, buffer);
        assertEquals("name: Name2\nnumber: 4\n", buffer.toString());
        String output = dumper.dump(bean);
        assertEquals("name: Name2\nnumber: 4\n", output);
    }

    public void testDumpObjectNullRepresenter() {
        try {
            new JavaBeanDumper(null, new DumperOptions());
            fail();
        } catch (NullPointerException e) {
            assertEquals("Representer must be provided.", e.getMessage());
        }
    }

    public void testDumpObjectNullOptions() {
        try {
            new JavaBeanDumper(new Representer(), null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("DumperOptions must be provided.", e.getMessage());
        }
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
