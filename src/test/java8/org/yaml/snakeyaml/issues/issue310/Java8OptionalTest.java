/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue310;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Java8OptionalTest extends OptionalTesting {

    @BeforeClass
    public static void checkIllegalAccess() {
        try {
            Constructor<?> privateConstructor = Optional.class.getDeclaredConstructor(Object.class);
            privateConstructor.setAccessible(true);
            privateConstructor.newInstance("OptionalString");
        } catch (RuntimeException | ReflectiveOperationException e) {
            reflectiveAccessDenied = true;
        }
    }

    @Before
    public void skipIfReflectiveAccessDenied() {
        org.junit.Assume.assumeFalse(reflectiveAccessDenied);
    }

    @Test
    public void testJava8OptionalStringLoad() {
        loadOptionalString();
    }

    @Test
    public void testJava8OptionalDumpLoad() {
        dumpLoadOptional();
    }

}
