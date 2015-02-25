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
package org.yaml.snakeyaml.issues.issue143;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class GenericMapTest extends TestCase {

    public void testMap() throws Exception {
        BeanWithMap fact = new BeanWithMap();
        GenericMap<Integer> shash = fact.getMap();
        shash.put("toto", new Integer(10));
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
        // String txt = yaml.dump(fact);
        // assertTrue(txt.contains("org.yaml.snakeyaml.issues.issue143.GenericMapTest"));
    }

    public static class GenericMap<T> extends java.util.HashMap<String, T> {
        private static final long serialVersionUID = -6833859369398863926L;
    }

    public class BeanWithMap {
        public GenericMap<Integer> getMap() {
            return new GenericMap<Integer>();
        }
    }
}