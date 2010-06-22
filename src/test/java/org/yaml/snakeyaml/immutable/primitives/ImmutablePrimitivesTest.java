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

package org.yaml.snakeyaml.immutable.primitives;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Yaml;

public class ImmutablePrimitivesTest extends TestCase {

    public void testPrimitives() {
        Yaml yaml = new Yaml(new Loader(), new Dumper(new ImmutablePrimitivesRepresenter(),
                new DumperOptions()));
        BunchOfPrimitives bunch = new BunchOfPrimitives(10, 20L, 30.0f, 40.0, true, 'q',
                (short) 17, (byte) 13);
        String dump = yaml.dump(bunch);
        assertEquals("!!" + bunch.getClass().getCanonicalName()
                + " [10, 20, 30.0, 40.0, true,\n  q, 17, 13]\n", dump);
        Object loaded = yaml.load(dump);
        assertEquals(loaded.toString(), bunch, loaded);
    }
}
