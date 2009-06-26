/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml;

import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

public class JavaBeanWithNullValuesTest extends TestCase {

    public void testNotNull() throws Exception {
        String dumpStr = dumpJavaBeanWithNullValues(false);
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
        parsed = JavaBeanParser.load(dumpStr, JavaBeanWithNullValues.class);
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

    public void testNull() throws Exception {
        String dumpStr = dumpJavaBeanWithNullValues(true);
        Yaml yaml = new Yaml();
        JavaBeanWithNullValues parsed = (JavaBeanWithNullValues) yaml.load(dumpStr);
        assertNull(parsed.getString());
        //
        parsed = JavaBeanParser.load(dumpStr, JavaBeanWithNullValues.class);
        assertNull(parsed.getString());
    }

    public void testNullStringAndBoolean() throws Exception {
        JavaBeanWithNullValues javaBeanWithNullValues = new JavaBeanWithNullValues();
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        Yaml yaml = new Yaml(options);
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
        yaml = new Yaml();
        JavaBeanWithNullValues parsed = (JavaBeanWithNullValues) yaml.load(dumpStr);
        assertNull(" expect null, got " + parsed.getBoolean1(), parsed.getBoolean1());
        assertNull(" expect null, got " + parsed.getString(), parsed.getString());
    }

    public void testNoRootTag() throws Exception {
        JavaBeanWithNullValues javaBeanWithNullValues = new JavaBeanWithNullValues();
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setExplicitStart(true);
        options.setExplicitEnd(true);
        options.setExplicitRoot("tag:yaml.org,2002:map");
        Yaml yaml = new Yaml(options);
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
        assertFalse("No explicit root tag must be used.", dumpStr
                .contains("JavaBeanWithNullValues"));
        yaml = new Yaml();
        JavaBeanWithNullValues parsed = JavaBeanParser.load(dumpStr, JavaBeanWithNullValues.class);
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
        Yaml yaml = new Yaml(options);
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
}
