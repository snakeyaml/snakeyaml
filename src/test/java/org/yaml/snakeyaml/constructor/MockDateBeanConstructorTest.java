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
package org.yaml.snakeyaml.constructor;

import java.util.Date;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class MockDateBeanConstructorTest extends TestCase {

    public void testConstructor() {
        String className = "!!org.yaml.snakeyaml.constructor.MockDateBeanConstructorTest$DateBean {number: 24, date: 2009-07-24}";
        Yaml yaml = new Yaml();
        try {
            yaml.load(className);
            fail("MockDate cannot be constructed.");
        } catch (Exception e) {
            assertEquals(
                    "Cannot construct: 'class org.yaml.snakeyaml.constructor.MockDateBeanConstructorTest$MockDate'",
                    e.getCause().getMessage());
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

        @Override
        public String toString() {
            return "<DateBean n=" + number + ">";
        }
    }

    public static class MockDate extends Date {
        private static final long serialVersionUID = 621384692653658062L;

        public MockDate(long date) {
            throw new RuntimeException("Test error.");
        }
    }
}
