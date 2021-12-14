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
package org.yaml.snakeyaml.error;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WrappedExceptionsTest {

    private static final String INVALID_YAML = "!!seq abc";

    @Test
    public void testWrapped() {
        try {
            LoaderOptions options = new LoaderOptions();
            options.setWrappedToRootException(true);
            Yaml yaml = new Yaml(options);
            yaml.load(INVALID_YAML);
            fail();
        } catch (YAMLException e) {
            assertEquals(ClassCastException.class, e.getCause().getClass());
        }
    }

    @Test(expected = ClassCastException.class)
    public void testUnWrapped() {
        LoaderOptions options = new LoaderOptions();
        options.setWrappedToRootException(false);
        Yaml yaml = new Yaml(options);
        yaml.load(INVALID_YAML);
    }
}
