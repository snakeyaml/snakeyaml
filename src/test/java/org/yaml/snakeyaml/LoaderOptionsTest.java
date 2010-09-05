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

package org.yaml.snakeyaml;

import junit.framework.TestCase;

import org.yaml.snakeyaml.LoaderOptions.ImplicitMode;

public class LoaderOptionsTest extends TestCase {

    public void testGetMode() {
        LoaderOptions defaultOptions = new LoaderOptions();
        assertFalse(defaultOptions.isWithMarkContext());
        assertEquals(LoaderOptions.ImplicitMode.DYNAMIC_IMPLICIT_TYPES, defaultOptions
                .getImplicitMode());
    }

    public void testUseImplicitTypes1() {
        LoaderOptions options = new LoaderOptions();
        assertTrue(options.useImplicitTypes());

    }

    public void testUseImplicitTypes2() {
        LoaderOptions options = new LoaderOptions(new TypeDescription(Object.class));
        assertTrue(options.useImplicitTypes());
    }

    public void testUseImplicitTypes3() {
        LoaderOptions options = new LoaderOptions(new TypeDescription(LoaderOptionsTest.class));
        assertFalse(options.useImplicitTypes());
    }

    public void testMoImplicitTypes() {
        LoaderOptions options = new LoaderOptions();
        options.setImplicitMode(ImplicitMode.NEVER_IMPLICIT_TYPES);
        Yaml yaml = new Yaml(options);
        String number = (String) yaml.load("11");
        assertEquals("Integer must not be recognised.", "11", number);
    }
}
