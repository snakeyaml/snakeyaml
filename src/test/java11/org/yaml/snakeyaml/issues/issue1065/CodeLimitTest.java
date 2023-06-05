/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.issues.issue1065;

import java.util.Iterator;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import static org.junit.Assert.*;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1065
 */
public class CodeLimitTest {
    String sdoc = "document: doc length <> code point size due to these chars 🎉🚀👀☀️\n";

    @Test
    public void exactCodePoints() {
        assertEquals(68, sdoc.length());
        assertEquals(65, sdoc.codePoints().count());

        LoaderOptions loaderOpts = new LoaderOptions();
        loaderOpts.setCodePointLimit(65);
        Yaml yaml = new Yaml(loaderOpts);

        Object doc = yaml.load(sdoc);
        assertNotNull(doc);
    }

    @Test
    public void oneLessCodePoints() {
        LoaderOptions loaderOpts = new LoaderOptions();
        loaderOpts.setCodePointLimit(65 - 1);
        Yaml yaml = new Yaml(loaderOpts);
        try {
            yaml.load(sdoc);
            fail("Small limit should be be accepted.");
        } catch (Exception e) {
            assertEquals("The incoming YAML document exceeds the limit: 64 code points.", e.getMessage());
        }
    }
}