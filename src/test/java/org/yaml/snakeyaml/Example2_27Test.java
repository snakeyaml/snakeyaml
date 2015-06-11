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
package org.yaml.snakeyaml;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.Constructor;

/**
 * Test Example 2.27 from the YAML specification
 * 
 * @see http://yaml.org/spec/1.1/
 */
public class Example2_27Test extends TestCase {

    public void testExample_2_27() {
        Yaml yaml = new Yaml(new Constructor(Invoice.class));
        Invoice invoice = (Invoice) yaml.load(Util
                .getLocalResource("specification/example2_27.yaml"));
        assertNotNull(invoice);
        Person billTo = invoice.billTo;
        assertEquals("Dumars", billTo.family);
        yaml = new Yaml();
        String output = yaml.dump(invoice);
        String etalon = Util.getLocalResource("specification/example2_27_dumped.yaml");
        assertEquals(etalon, output);
    }
}
