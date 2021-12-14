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
package org.yaml.snakeyaml.issues.issue395;

import static junit.framework.TestCase.assertTrue;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

public class ReportPositionForDuplicateKeyTest {

    @Test
    public void deleteDuplicatKeysInCorrectOrder() {
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setAllowDuplicateKeys(false);
        Yaml yaml = new Yaml(
                new Constructor(),
                new Representer(),
                new DumperOptions(),
                loaderOptions,
                new Resolver());
        try {
            yaml.load("key1: a\nkey1: b");
        } catch (DuplicateKeyException e) {
            assertTrue(e.getMessage(), e.getMessage().contains("found duplicate key key1"));
            assertTrue(e.getMessage(), e.getMessage().contains("line 1, column 1"));
        }
    }
}
