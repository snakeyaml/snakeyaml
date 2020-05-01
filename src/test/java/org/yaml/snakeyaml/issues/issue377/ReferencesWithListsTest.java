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
package org.yaml.snakeyaml.issues.issue377;

import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

import static org.junit.Assert.*;

public class ReferencesWithListsTest {

    @Test
    public void referencesWithRecursiveKeysInListAllowedByDefault() {
        String output =
                "a:\n" +
                        "  b: &test\n" +
                        "  - *test";
        Yaml yaml = new Yaml();
        //System.out.println(output);
        Map<String, Object> parsed = yaml.load(output);
        assertNotNull(output, parsed);
        assertFalse(output, parsed.isEmpty());
    }

    @Test
    public void referencesWithRecursiveKeysInList() {
        String output =
                "a:\n" +
                        "  b: &test\n" +
                        "  - *test";
        LoaderOptions settings = new LoaderOptions();
        settings.setAllowRecursiveKeys(false);
        Yaml yaml = new Yaml(settings);
        System.out.println(output);

        try {
            yaml.load(output);
            fail("Should not have been reached, expected an exception.");
        } catch (Exception e) {
            assertEquals("Recursive key for mapping is detected but it is not configured to be allowed.", e.getMessage());
        }
    }


}