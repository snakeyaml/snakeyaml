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
package org.yaml.snakeyaml.issues.issue40;

import java.math.BigDecimal;

import junit.framework.TestCase;

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
        Yaml yaml = new Yaml();
        String text = yaml.dumpAsMap(input);
        // System.out.println(text);
        assertEquals("decimal: 5.123\n", text);
        Yaml loader = new Yaml();
        DogFoodBean output = loader.loadAs(text, DogFoodBean.class);
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
