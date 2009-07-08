/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class MockDateBeanConstructorTest extends TestCase {

    public void testConstructor() throws IOException {
        String className = "!!org.yaml.snakeyaml.constructor.MockDateBeanConstructorTest$DateBean {number: 24, date: 2009-07-24}";
        Yaml yaml = new Yaml();
        try {
            yaml.load(className);
            fail("MockDate cannot be constructed.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("MockDate"));
        }
    }

    public static class DateBean {
        private int number;
        private MockDate date;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public MockDate getDate() {
            return date;
        }

        public void setDate(MockDate date) {
            this.date = date;
        }
    }

    public static class MockDate extends Date {
        private static final long serialVersionUID = 621384692653658062L;

        public MockDate(long date) {
            throw new RuntimeException("Test error.");
        }
    }
}
