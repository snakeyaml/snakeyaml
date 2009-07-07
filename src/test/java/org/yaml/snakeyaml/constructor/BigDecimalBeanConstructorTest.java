/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.io.IOException;
import java.math.BigDecimal;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class BigDecimalBeanConstructorTest extends TestCase {

    public void testRepresentor() throws IOException {
        BigDecimalJavaBean bean = new BigDecimalJavaBean();
        bean.setAmount(1.5f);
        bean.setNumber(new BigDecimal("3.1416"));
        Yaml yaml = new Yaml();
        String output = yaml.dump(bean);
        String className = this.getClass().getPackage().getName();
        assertEquals("!!" + className + ".BigDecimalJavaBean {amount: 1.5, number: 3.1416}\n",
                output);
    }

    public void testConstructor() throws IOException {
        String className = "!!" + this.getClass().getPackage().getName()
                + ".BigDecimalJavaBean {amount: 1.5, number: 3.1416}";
        Yaml yaml = new Yaml();
        BigDecimalJavaBean bean = (BigDecimalJavaBean) yaml.load(className);
        assertNotNull(bean);
        assertTrue(1.5 - bean.getAmount() < 0.0000001);
        assertTrue((new BigDecimal("3.1416")).add(bean.getNumber().negate()).doubleValue() < 0.0000001);
    }
}
