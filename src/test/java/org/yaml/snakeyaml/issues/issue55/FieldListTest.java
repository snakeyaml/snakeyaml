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
package org.yaml.snakeyaml.issues.issue55;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class FieldListTest extends TestCase {

    public void testYaml() {
        Yaml beanLoader = new Yaml();
        beanLoader.setBeanAccess(BeanAccess.FIELD);
        BlogField rehydrated = beanLoader.loadAs(Util.getLocalResource("issues/issue55_2.txt"),
                BlogField.class);
        assertEquals(4, rehydrated.getPosts().size());
    }

    public void testFailureWithoutFieldAccess() {
        Yaml beanLoader = new Yaml();
        try {
            beanLoader.loadAs(Util.getLocalResource("issues/issue55_2.txt"), BlogField.class);
            fail("Private field must not be available");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Unable to find property 'posts'"));
        }
    }

    public static class BlogField {
        private List<Integer> posts;

        public BlogField() {
            posts = new LinkedList<Integer>();
        }

        public List<Integer> getPosts() {
            return posts;
        }
    }
}
