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

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class BigDecimalBeanConstructorTest extends TestCase {

    public void testRepresentor() {
        BigDecimalJavaBean bean = new BigDecimalJavaBean();
        bean.setAmount(1.5f);
        bean.setNumber(new BigDecimal("3.1416"));
        Yaml yaml = new Yaml();
        String output = yaml.dump(bean);
        String className = this.getClass().getPackage().getName();
        assertEquals("!!" + className + ".BigDecimalJavaBean {amount: 1.5, number: 3.1416}\n",
                output);
    }

    public void testConstructor() {
        String className = "!!" + this.getClass().getPackage().getName()
                + ".BigDecimalJavaBean {amount: 1.5, number: 3.1416}";
        Yaml yaml = new Yaml();
        BigDecimalJavaBean bean = (BigDecimalJavaBean) yaml.load(className);
        assertNotNull(bean);
        assertTrue(1.5 - bean.getAmount() < 0.0000001);
        assertTrue((new BigDecimal("3.1416")).add(bean.getNumber().negate()).doubleValue() < 0.0000001);
    }

    public void testConstructorAtomic() {
        String className = "!!" + this.getClass().getPackage().getName()
                + ".AtomicJavaBean {amount: 1.5, atomic: 0}";
        Yaml yaml = new Yaml();
        try {
            yaml.load(className);
            fail("AtomicLong is not supported.");
        } catch (Exception e) {
            assertEquals("argument type mismatch", e.getCause().getMessage());
        }
    }
}
