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
package org.yaml.snakeyaml.issues.issue183;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class NumberBeanTest extends TestCase {

    public void testNumberBean() throws Exception {

        NumberBean number = new NumberBean();
        number.number = 1;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(new Long(1), loaded.number);
    }

    public void testNumberAsFloatInfinity() throws Exception {
        NumberBean number = new NumberBean();
        number.number = Float.POSITIVE_INFINITY;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(Float.POSITIVE_INFINITY, loaded.number.floatValue());
    }

    public void testNumberAsDoubleInfinity() throws Exception {
        NumberBean number = new NumberBean();
        number.number = Double.POSITIVE_INFINITY;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(Double.POSITIVE_INFINITY, loaded.number.doubleValue());
    }

    public void testNumberAsNegativeFloatInfinity() throws Exception {
        NumberBean number = new NumberBean();
        number.number = Float.NEGATIVE_INFINITY;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(Float.NEGATIVE_INFINITY, loaded.number.floatValue());
    }

    public void testNumberAsNegativeDoubleInfinity() throws Exception {
        NumberBean number = new NumberBean();
        number.number = Double.NEGATIVE_INFINITY;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(Double.NEGATIVE_INFINITY, loaded.number.doubleValue());
    }

    public void testNumberAsFloatNaN() throws Exception {
        NumberBean number = new NumberBean();
        number.number = Float.NaN;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(Float.NaN, loaded.number.floatValue());
    }

    public void testNumberAsDoubleNaN() throws Exception {
        NumberBean number = new NumberBean();
        number.number = Double.NaN;

        Yaml yaml = new Yaml();
        String dump = yaml.dump(number);
        NumberBean loaded = yaml.loadAs(dump, NumberBean.class);
        assertEquals(Double.NaN, loaded.number.doubleValue());
    }

}
