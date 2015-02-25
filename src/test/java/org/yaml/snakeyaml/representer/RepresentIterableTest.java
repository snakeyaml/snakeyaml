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
package org.yaml.snakeyaml.representer;

import java.util.Iterator;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

/**
 * Test {@link issue69 http://code.google.com/p/snakeyaml/issues/detail?id=69}
 */
public class RepresentIterableTest extends TestCase {

    public void testIterable() {
        Yaml yaml = new Yaml();
        try {
            yaml.dump(new CounterFactory());
            fail("Iterable should not be treated as sequence by default.");
        } catch (Exception e) {
            assertEquals(
                    "No JavaBean properties found in org.yaml.snakeyaml.representer.RepresentIterableTest$CounterFactory",
                    e.getMessage());
        }
    }

    public void testIterator() {
        Yaml yaml = new Yaml();
        String output = yaml.dump(new Counter(7));
        assertEquals("[0, 1, 2, 3, 4, 5, 6]\n", output);
    }

    private class CounterFactory implements Iterable<Integer> {
        public Iterator<Integer> iterator() {
            return new Counter(10);
        }
    }

    private class Counter implements Iterator<Integer> {
        private int max = 0;
        private int counter = 0;

        public Counter(int max) {
            this.max = max;
        }

        public boolean hasNext() {
            return counter < max;
        }

        public Integer next() {
            return counter++;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
