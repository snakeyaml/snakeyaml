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
package org.yaml.snakeyaml.error;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

public class WrappedExceptionsTest {

    private static final String INVALID_YAML = "!!seq abc";

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void configureExpectedExceptions() {
        expectedException.expectMessage("org.yaml.snakeyaml.nodes.ScalarNode");
        expectedException.expectMessage("org.yaml.snakeyaml.nodes.SequenceNode");
    }

    @Test
    public void testWrapped() {
        expectedException.expect(YAMLException.class);
        expectedException
                .expectCause(CoreMatchers.<Throwable> instanceOf(ClassCastException.class));

        LoaderOptions options = new LoaderOptions();
        options.setWrappedToRootException(true);
        Yaml yaml = new Yaml(options);
        yaml.load(INVALID_YAML);
    }

    @Test
    public void testUnWrapped() {
        expectedException.expect(ClassCastException.class);

        LoaderOptions options = new LoaderOptions();
        options.setWrappedToRootException(false);
        Yaml yaml = new Yaml(options);
        yaml.load(INVALID_YAML);
    }
}
