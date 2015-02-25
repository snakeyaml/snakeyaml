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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @see http://yaml.org/type/int.html
 */
public class IntTagTest extends AbstractTest {

    public void testInt() {
        assertEquals(new Integer(685230), getMapValue("canonical: 685230", "canonical"));
        assertEquals(new Integer(685230), getMapValue("number: 685_230", "number"));
        assertEquals(new Integer(685230), getMapValue("decimal: +685230", "decimal"));
        assertEquals(new Integer(-685230), getMapValue("number: -685230", "number"));
        assertEquals(new Integer(685230), getMapValue("octal: 02472256", "octal"));
        assertEquals(new Integer(685230), getMapValue("hexadecimal: 0x_0A_74_AE", "hexadecimal"));
        assertEquals(new Integer(685230),
                getMapValue("binary: 0b1010_0111_0100_1010_1110", "binary"));
        assertEquals(new Integer(685230), getMapValue("sexagesimal: 190:20:30", "sexagesimal"));
        assertEquals(new Integer(0), load("0"));
        assertEquals(new Integer(0), load("-0"));
        assertEquals(new Integer(0), load("+0"));
        assertEquals(Integer.MIN_VALUE, load(dump(Integer.MIN_VALUE)));
        assertEquals(Integer.MAX_VALUE, load(dump(Integer.MAX_VALUE)));
    }

    public void testBigInt() {
        assertEquals(new Long(922337203685477580L), load("922337203685477580"));
        assertEquals(new BigInteger("9223372036854775809999999999"),
                load("9223372036854775809999999999"));
        assertEquals(Long.MIN_VALUE, load("-9223372036854775808"));
    }

    public void testIntShorthand() {
        assertEquals(new Integer(1), getMapValue("number: !!int 1", "number"));
    }

    public void testIntTag() {
        assertEquals(new Integer(1), getMapValue("number: !<tag:yaml.org,2002:int> 1", "number"));
    }

    public void testIntOut() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("number", new Integer(1));
        String output = dump(map);
        assertTrue(output.contains("number: 1"));
    }
}
