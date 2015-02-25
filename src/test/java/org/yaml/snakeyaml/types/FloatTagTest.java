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
package org.yaml.snakeyaml.types;

import java.util.HashMap;
import java.util.Map;

/**
 * @see http://yaml.org/type/float.html
 */
public class FloatTagTest extends AbstractTest {

    public void testFloat() {
        assertEquals(new Double(6.8523015e+5), getMapValue("canonical: 6.8523015e+5", "canonical"));
        assertEquals(new Double(6.8523015e+5),
                getMapValue("exponentioal: 685.230_15e+03", "exponentioal"));
        assertEquals(new Double(6.8523015e+5), getMapValue("fixed: 685_230.15", "fixed"));
        assertEquals(new Double(6.8523015e+5),
                getMapValue("sexagesimal: 190:20:30.15", "sexagesimal"));
        assertEquals(Double.NEGATIVE_INFINITY,
                getMapValue("negative infinity: -.inf", "negative infinity"));
        assertEquals(Double.NaN, getMapValue("not a number: .NaN", "not a number"));
    }

    public void testFloatShorthand() {
        assertEquals(new Double(1), getMapValue("number: !!float 1", "number"));
    }

    public void testFloatTag() {
        assertEquals(new Double(1), getMapValue("number: !<tag:yaml.org,2002:float> 1", "number"));
    }

    public void testFloatOut() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("number", new Double(1));
        String output = dump(map);
        assertEquals("{number: 1.0}\n", output);
    }

    public void testBasicDoubleScalarLoad() {
        assertEquals(new Double(47.0), load("47.0"));
        assertEquals(new Double(0.0), load("0.0"));
        assertEquals(new Double(-1.0), load("-1.0"));
    }

    public void testDumpStr() {
        assertEquals("'1.0'\n", dump("1.0"));
    }

    public void testDump() {
        assertEquals("1.0\n", dump(1.0));
    }

    /**
     * to test http://code.google.com/p/snakeyaml/issues/detail?id=130
     */
    public void testScientificFloatWithoutDecimalDot() {
        assertEquals(new Double(8e-06), load("8e-06"));
        assertEquals(new Double(8e06), load("8e06"));
        assertEquals(new Double(8e06), load("8e+06"));
        assertEquals(new Double(8000e06), load("8_000e06"));
        assertEquals(new Double(123e-06), load("123e-06"));
    }
}
