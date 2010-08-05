/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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

package org.yaml.snakeyaml.immutable;

import junit.framework.TestCase;

import org.yaml.snakeyaml.SnakeYaml;

public class DogImmutableTest extends TestCase {

    public void testDog() {
        SnakeYaml yaml = new SnakeYaml();
        Dog loaded = (Dog) yaml.load("!!org.yaml.snakeyaml.immutable.Dog Bulldog");
        assertEquals("Bulldog", loaded.getName());
    }

    public void testHouse() {
        SnakeYaml yaml = new SnakeYaml();
        HouseBean loaded = (HouseBean) yaml
                .load("!!org.yaml.snakeyaml.immutable.HouseBean\nanimal: !!org.yaml.snakeyaml.immutable.Dog Bulldog");
        assertEquals("Bulldog", loaded.getAnimal().getName());
    }
}
