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

public class BinaryJavaBeanTest extends TestCase {
    public void testBeanTest() {
        BinaryBean bean = new BinaryBean();
        bean.setId(1);
        byte[] bytes = new byte[] { 1, 7, 9, 31, 65 };
        bean.setData(bytes);
        Yaml yaml = new Yaml();
        String output = yaml.dump(bean);
        String etalon = "!!org.yaml.snakeyaml.BinaryBean\ndata: !!binary |-\n  AQcJH0E=\nid: 1\n";
        assertEquals(etalon, output);
        // load
        BinaryBean bean2 = (BinaryBean) yaml.load(output);
        assertEquals(1, bean2.getId());
        assertEquals(new String(bytes), new String(bean2.getData()));
    }
}
