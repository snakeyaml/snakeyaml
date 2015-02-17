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

import java.util.Collection;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

public class YamlFieldAccessCollectionTest extends TestCase {

    public void testYaml() {
        Blog original = createTestBlog();
        Yaml yamlDumper = constructYamlDumper();
        String serialized = yamlDumper.dumpAsMap(original);
        // System.out.println(serialized);
        assertEquals(Util.getLocalResource("issues/issue55_1.txt"), serialized);
        Yaml blogLoader = new Yaml();
        blogLoader.setBeanAccess(BeanAccess.FIELD);
        Blog rehydrated = blogLoader.loadAs(serialized, Blog.class);
        checkTestBlog(rehydrated);
    }

    @SuppressWarnings("unchecked")
    public void testYamlWithoutConfiguration() {
        Yaml yaml = new Yaml();
        Map<String, Object> map = (Map<String, Object>) yaml.load(Util
                .getLocalResource("issues/issue55_1.txt"));
        assertEquals(1, map.size());
    }

    public void testYamlFailure() {
        Yaml beanLoader = new Yaml();
        try {
            beanLoader.loadAs(Util.getLocalResource("issues/issue55_1.txt"), Blog.class);
            fail("BeanAccess.FIELD is required.");
        } catch (Exception e) {
            assertTrue(e.getMessage(), e.getMessage().contains("Unable to find property 'posts'"));
        }
    }

    public void testYamlDefaultWithFeildAccess() {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        Blog original = createTestBlog();
        String serialized = yaml.dump(original);
        assertEquals(Util.getLocalResource("issues/issue55_1_rootTag.txt"), serialized);
        Blog rehydrated = (Blog) yaml.load(serialized);
        checkTestBlog(rehydrated);
    }

    protected Yaml constructYamlDumper() {
        Representer representer = new Representer();
        representer.getPropertyUtils().setBeanAccess(BeanAccess.FIELD);
        Yaml yaml = new Yaml(representer);
        return yaml;
    }

    protected Yaml constructYamlParser() {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml;
    }

    protected Blog createTestBlog() {
        Post post1 = new Post("Test", "Dummy");
        Post post2 = new Post("Highly", "Creative");
        Blog blog = new Blog();
        blog.addPost(post1);
        blog.addPost(post2);
        return blog;
    }

    protected void checkTestBlog(Blog blog) {
        Collection<Post> posts = blog.getPosts();
        assertEquals("Blog contains 2 posts", 2, posts.size());
    }
}
