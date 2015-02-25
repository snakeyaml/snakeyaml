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
package org.yaml.snakeyaml.immutable.primitives;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class ImmutablePrimitivesTest extends TestCase {

    public void testPrimitives() {
        Yaml yaml = new Yaml(new ImmutablePrimitivesRepresenter());
        BunchOfPrimitives bunch = new BunchOfPrimitives(10, 40.0, true);
        String dump = yaml.dump(bunch);
        assertEquals("!!" + bunch.getClass().getCanonicalName() + " [10, 40.0, true]\n", dump);
        Object loaded = yaml.load(dump);
        assertEquals(loaded.toString(), bunch, loaded);
    }

    public void testPrimitivesLong() {
        Yaml yaml = new Yaml();
        String dump = "!!org.yaml.snakeyaml.immutable.primitives.BunchOfPrimitives [10000000000, 40.0, true]";
        BunchOfPrimitives bunch = (BunchOfPrimitives) yaml.load(dump);
        assertEquals("Must be truncated.", new Long(10000000000L).intValue(),
                bunch.getPrimitiveInt());
    }

    public void testPrimitivesException() {
        Yaml yaml = new Yaml();
        String dump = "!!org.yaml.snakeyaml.immutable.primitives.BunchOfPrimitives [10, 40, true]";
        try {
            yaml.load(dump);
            fail();
        } catch (YAMLException e) {
            assertTrue(e
                    .getMessage()
                    .startsWith(
                            "Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.immutable.primitives.BunchOfPrimitives; exception=No suitable constructor with 3 arguments found for class org.yaml.snakeyaml.immutable.primitives.BunchOfPrimitives"));
        }
    }
}
