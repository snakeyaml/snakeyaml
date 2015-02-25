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

import org.yaml.snakeyaml.constructor.ArrayTagsTest.CarWithArray;
import org.yaml.snakeyaml.nodes.Tag;

public class TypeDescriptionTest extends TestCase {

    public void testSetTag() {
        TypeDescription descr = new TypeDescription(TypeDescriptionTest.class);
        descr.setTag("!bla");
        assertEquals(new Tag("!bla"), descr.getTag());
        descr.setTag(new Tag("!foo"));
        assertEquals(new Tag("!foo"), descr.getTag());
    }

    public void testToString() {
        TypeDescription carDescription = new TypeDescription(CarWithArray.class, "!car");
        assertEquals(
                "TypeDescription for class org.yaml.snakeyaml.constructor.ArrayTagsTest$CarWithArray (tag='!car')",
                carDescription.toString());
    }
}
