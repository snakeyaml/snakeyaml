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

package org.yaml.snakeyaml.issues.issue73;

import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class SetAsSequenceTest extends TestCase {

    public void testDump() {
        Blog blog = new Blog("Test Me!");
        blog.addPost(new Post("Title1", "text 1"));
        blog.addPost(new Post("Title2", "text text 2"));
        blog.numbers.add(19);
        blog.numbers.add(17);
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(new Dumper(options));
        String output = yaml.dump(blog);
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue73-1.txt"), output);
    }

    public void testLoad() {
        Loader loader = new Loader();
        loader.setBeanAccess(BeanAccess.FIELD);
        Yaml yaml = new Yaml(loader);
        String doc = Util.getLocalResource("issues/issue73-1.txt");
        Blog blog = (Blog) yaml.load(doc);
        // System.out.println(blog);
        assertEquals("Test Me!", blog.getName());
        assertEquals(2, blog.numbers.size());
        assertEquals(2, blog.getPosts().size());
        for (Post post : blog.getPosts()) {
            assertEquals(Post.class, post.getClass());
        }
    }

    public void testYaml() {
        String serialized = Util.getLocalResource("issues/issue73-2.txt");
        // System.out.println(serialized);
        JavaBeanLoader<Blog> beanLoader = new JavaBeanLoader<Blog>(Blog.class, BeanAccess.FIELD);
        Blog rehydrated = beanLoader.load(serialized);

        checkTestBlog(rehydrated);
    }

    protected void checkTestBlog(Blog blog) {
        Set<Post> posts = blog.getPosts();
        assertEquals("Blog contains 2 posts", 2, posts.size());
        for (Post post : posts) {
            assertEquals(Post.class, post.getClass());
        }
        assertEquals("No tags!", blog.getName());
        assertEquals(0, blog.numbers.size());
    }
}
