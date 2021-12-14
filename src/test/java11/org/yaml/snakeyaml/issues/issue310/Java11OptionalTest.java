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

import static org.hamcrest.CoreMatchers.instanceOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.yaml.snakeyaml.error.YAMLException;

public class Java11OptionalTest extends OptionalTesting {

    private static Logger log = Logger.getLogger(Java11OptionalTest.class.getPackageName());

    @BeforeClass
    public static void checkIllegalAccess() {
        try {
            Constructor<?> privateConstructor = Optional.class.getDeclaredConstructor(Object.class);
            privateConstructor.setAccessible(true);
            privateConstructor.newInstance("OptionalString");
        } catch (InaccessibleObjectException | ReflectiveOperationException | SecurityException e) {
            log.warning(
                    "Expecting exceptions in these tests because reflective access has been denied: "
                            + e.getLocalizedMessage());
            reflectiveAccessDenied = true;
        }
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void configureExpectedExceptions() {
        if (reflectiveAccessDenied) {
            expectedException.expect(YAMLException.class);
            expectedException.expect(
                    new DeepThrowableCauseMatcher(instanceOf(InaccessibleObjectException.class)));
        }
    }

    @Test
    public void testJava11OptionalStringLoad() {
        loadOptionalString();
    }

    @Test
    public void testJava11OptionalDumpLoad() {
        dumpLoadOptional();
    }
}
