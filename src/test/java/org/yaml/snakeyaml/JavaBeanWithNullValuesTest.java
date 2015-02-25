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
package org.yaml.snakeyaml;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class JavaBeanWithNullValuesTest extends TestCase {
    private Yaml loader;

    @Override
    protected void setUp() {
        loader = new Yaml();
    }

    public void testNotNull() {
        String dumpStr = dumpJavaBeanWithNullValues(false);
        // System.out.println(dumpStr);
        Yaml yaml = new Yaml();
        JavaBeanWithNullValues parsed = (JavaBeanWithNullValues) yaml.load(dumpStr);
        assertNotNull(parsed.getString());
        assertNotNull(parsed.getBoolean1());
        assertNotNull(parsed.getDate());
        assertNotNull(parsed.getDouble1());
        assertNotNull(parsed.getFloat1());
        assertNotNull(parsed.getInteger());
        assertNotNull(parsed.getLong1());
        assertNotNull(parsed.getSqlDate());
        assertNotNull(parsed.getTimestamp());
        //
        parsed = loader.loadAs(dumpStr, JavaBeanWithNullValues.class);
        assertNotNull(parsed.getString());
        assertNotNull(parsed.getBoolean1());
        assertNotNull(parsed.getDate());
        assertNotNull(parsed.getDouble1());
        assertNotNull(parsed.getFloat1());
        assertNotNull(parsed.getInteger());
        assertNotNull(parsed.getLong1());
        assertNotNull(parsed.getSqlDate());
        assertNotNull(parsed.getTimestamp());
    }

    public void testNull() {
        String dumpStr = dumpJavaBeanWithNullValues(true);
        Yaml yaml = new Yaml();
        JavaBeanWithNullValues parsed = (JavaBeanWithNullValues) yaml.load(dumpStr);
        assertNull(parsed.getString());
        //
        parsed = loader.loadAs(dumpStr, JavaBeanWithNullValues.class);
        assertNull(parsed.getString());
    }

    public void testNullStringAndBoolean() {
        JavaBeanWithNullValues javaBeanWithNullValues = new JavaBeanWithNullValues();
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        Yaml yaml = new Yaml(new CustomRepresenter(), options);
        javaBeanWithNullValues.setBoolean1(null);
        javaBeanWithNullValues.setDate(new Date(System.currentTimeMillis()));
        javaBeanWithNullValues.setDouble1(1d);
        javaBeanWithNullValues.setFloat1(1f);
        javaBeanWithNullValues.setInteger(1);
        javaBeanWithNullValues.setLong1(1l);
        javaBeanWithNullValues.setSqlDate(new java.sql.Date(System.currentTimeMillis()));
        javaBeanWithNullValues.setString(null); // ok
        javaBeanWithNullValues.setTimestamp(new Timestamp(System.currentTimeMillis()));

        String dumpStr = yaml.dump(javaBeanWithNullValues);
        // System.out.println(dumpStr);
        yaml = new Yaml();
        JavaBeanWithNullValues parsed = (JavaBeanWithNullValues) yaml.load(dumpStr);
        assertNull(" expect null, got " + parsed.getBoolean1(), parsed.getBoolean1());
        assertNull(" expect null, got " + parsed.getString(), parsed.getString());
    }

    public void testNoRootTag() {
        JavaBeanWithNullValues javaBeanWithNullValues = new JavaBeanWithNullValues();
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        Yaml yaml = new Yaml(new CustomRepresenter(), options);
        javaBeanWithNullValues.setBoolean1(null);
        javaBeanWithNullValues.setDate(new Date(System.currentTimeMillis()));
        javaBeanWithNullValues.setDouble1(1d);
        javaBeanWithNullValues.setFloat1(1f);
        javaBeanWithNullValues.setInteger(1);
        javaBeanWithNullValues.setLong1(1l);
        javaBeanWithNullValues.setSqlDate(new java.sql.Date(System.currentTimeMillis()));
        javaBeanWithNullValues.setString(null); // ok
        javaBeanWithNullValues.setTimestamp(new Timestamp(System.currentTimeMillis()));

        String dumpStr = yaml.dumpAsMap(javaBeanWithNullValues);
        // System.out.println(dumpStr);
        assertFalse("No explicit root tag must be used.",
                dumpStr.contains("JavaBeanWithNullValues"));
        yaml = new Yaml(new CustomRepresenter(), options);
        JavaBeanWithNullValues parsed = loader.loadAs(dumpStr, JavaBeanWithNullValues.class);
        assertNull(" expect null, got " + parsed.getBoolean1(), parsed.getBoolean1());
        assertNull(" expect null, got " + parsed.getString(), parsed.getString());
        assertEquals(1d, parsed.getDouble1());
        assertEquals(1f, parsed.getFloat1());
        assertEquals(new Integer(1), parsed.getInteger());
        assertEquals(new Long(1l), parsed.getLong1());
    }

    private String dumpJavaBeanWithNullValues(boolean nullValues) {
        JavaBeanWithNullValues javaBeanWithNullValues = new JavaBeanWithNullValues();
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        Yaml yaml = new Yaml(new CustomRepresenter(), options);
        if (nullValues) {
            return yaml.dump(javaBeanWithNullValues);
        }
        javaBeanWithNullValues.setBoolean1(false);
        javaBeanWithNullValues.setDate(new Date(System.currentTimeMillis()));
        javaBeanWithNullValues.setDouble1(1d);
        javaBeanWithNullValues.setFloat1(1f);
        javaBeanWithNullValues.setInteger(1);
        javaBeanWithNullValues.setLong1(1l);
        javaBeanWithNullValues.setSqlDate(new java.sql.Date(System.currentTimeMillis()));
        javaBeanWithNullValues.setString(""); // ok
        javaBeanWithNullValues.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return yaml.dump(javaBeanWithNullValues);
    }

    public class CustomRepresenter extends Representer {
        public CustomRepresenter() {
            this.representers.put(Float.class, new RepresentFloat());
            this.representers.put(Long.class, new RepresentLong());
            this.representers.put(java.sql.Date.class, new RepresentDate());
            this.representers.put(java.sql.Timestamp.class, new RepresentTime());
        }

        private class RepresentFloat implements Represent {
            public Node representData(Object data) {
                return representScalar(new Tag(Tag.PREFIX + "java.lang.Float"),
                        ((Float) data).toString());
            }
        }

        private class RepresentLong implements Represent {
            public Node representData(Object data) {
                return representScalar(new Tag(Tag.PREFIX + "java.lang.Long"),
                        ((Long) data).toString());
            }
        }

        private class RepresentDate implements Represent {
            public Node representData(Object data) {
                return representScalar(new Tag(Tag.PREFIX + "java.sql.Date"),
                        ((java.sql.Date) data).toString());
            }
        }

        private class RepresentTime implements Represent {
            public Node representData(Object data) {
                return representScalar(new Tag(Tag.PREFIX + "java.sql.Timestamp"),
                        ((java.sql.Timestamp) data).toString());
            }
        }
    }
}
