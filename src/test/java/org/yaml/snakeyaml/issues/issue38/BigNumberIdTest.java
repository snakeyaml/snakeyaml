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
package org.yaml.snakeyaml.issues.issue38;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * to test http://code.google.com/p/snakeyaml/issues/detail?id=38
 */
public class BigNumberIdTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testBigNumberFormat() {
        List<Bean> list = new ArrayList<Bean>(2000);
        for (int i = 1; i < 1010; i++) {
            Bean value = new Bean(i);
            list.add(value);
            list.add(value);
        }
        Yaml yaml = new Yaml();
        String output = yaml.dump(list);
        // System.out.println(output);
        //
        List<Bean> list2 = (List<Bean>) yaml.load(output);
        for (Bean bean : list2) {
            assertTrue(bean.getValue() > 0);
        }
    }
}
