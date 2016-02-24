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
package org.yaml.snakeyaml.issues.issue332;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class DataTest {

    @Test
    public void testGetUnit() throws Exception {
        Data data = new Data("Voltage", BigDecimal.TEN, "V");
        assertEquals("!!org.yaml.snakeyaml.issues.issue332.Data {}", new Yaml().dump(data).trim());
        //TODO assertEquals("!!org.yaml.snakeyaml.issues.issue332.Data {label: Voltage, unit: V, value: !!float '10'}", new Yaml().dump(data).trim());
    }

    @Test
    public void testLoad() throws Exception {
        String doc = "!!org.yaml.snakeyaml.issues.issue332.Data [Voltage, 10, volts]";
        assertEquals("Data{label='Voltage', unit='volts', value=10}", (new Yaml().load(doc)).toString());
    }
}