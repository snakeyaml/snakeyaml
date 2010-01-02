package org.yaml.snakeyaml.issues.issue40;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Yaml;

public class DogFoodBeanTest extends TestCase {

    public void testOwnBigDecimal() {
        DogFoodBean input = new DogFoodBean();
        input.setDecimal(new BigDecimal("5"));
        Yaml yaml = new Yaml();
        String text = yaml.dump(input);
        // System.out.println(text);
        assertEquals("!!org.yaml.snakeyaml.issues.issue40.DogFoodBean {decimal: !!float '5'}\n",
                text);
        DogFoodBean output = (DogFoodBean) yaml.load(text);
        assertEquals(output.getDecimal(), input.getDecimal());
    }

    public void testBigDecimalPrecision() {
        DogFoodBean input = new DogFoodBean();
        input.setDecimal(new BigDecimal("5.123"));
        Yaml yaml = new Yaml();
        String text = yaml.dump(input);
        // System.out.println(text);
        assertEquals("!!org.yaml.snakeyaml.issues.issue40.DogFoodBean {decimal: 5.123}\n", text);
        DogFoodBean output = (DogFoodBean) yaml.load(text);
        assertEquals(input.getDecimal(), output.getDecimal());
    }

    public void testBigDecimalNoRootTag() {
        DogFoodBean input = new DogFoodBean();
        input.setDecimal(new BigDecimal("5.123"));
        JavaBeanDumper dumper = new JavaBeanDumper();
        String text = dumper.dump(input);
        // System.out.println(text);
        assertEquals("decimal: 5.123\n", text);
        JavaBeanLoader<DogFoodBean> loader = new JavaBeanLoader<DogFoodBean>(DogFoodBean.class);
        DogFoodBean output = (DogFoodBean) loader.load(text);
        assertEquals(input.getDecimal(), output.getDecimal());
    }

    public void testBigDecimal1() {
        Yaml yaml = new Yaml();
        String text = yaml.dump(new BigDecimal("5"));
        assertEquals("!!float '5'\n", text);
    }

    public void testBigDecimal2() {
        Yaml yaml = new Yaml();
        String text = yaml.dump(new BigDecimal("5.123"));
        assertEquals("5.123\n", text);
    }
}
