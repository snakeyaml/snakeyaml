/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue373;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;

/**
 * Redundant test - it does not test anything.
 * It is here only to prove that SnakeYAML does what it should.
 */
public class UnknownRepresenterTest {

    @Test
    public void testIndicatorIndentMuchSmaller() {
        ModelMapIntegerBigdecimal mv = new ModelMapIntegerBigdecimal();

        Map<Integer, BigDecimal> m = new HashMap<Integer, BigDecimal>();
        Integer a = 1;
        BigDecimal b = new BigDecimal(0.01);
        m.put(a, b);
        mv.setMapIntegerBigDecimal(m);

        Yaml yaml = new Yaml();
        String str = yaml.dump(mv);
        //System.out.println(str);
        assertTrue(str.contains("mapIntegerBigDecimal: {1: 0.01"));
    }
}
