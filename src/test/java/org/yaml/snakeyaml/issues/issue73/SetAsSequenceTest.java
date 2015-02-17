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
package org.yaml.snakeyaml.issues.issue73;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
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
        TreeSet<String> labels = new TreeSet<String>();
        labels.add("Java");
        labels.add("YAML");
        labels.add("SnakeYAML");
        blog.setLabels(labels);
        DumperOptions options = new DumperOptions();
        options.setAllowReadOnlyProperties(true);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(blog);
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue73-1.txt"), output);
    }

    public void testLoad() {
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
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
        Yaml beanLoader = new Yaml();
        beanLoader.setBeanAccess(BeanAccess.FIELD);
        Blog rehydrated = beanLoader.loadAs(serialized, Blog.class);
        checkTestBlog(rehydrated);
    }

    protected void checkTestBlog(Blog blog) {
        Set<Post> posts = blog.getPosts();
        assertEquals("Blog contains 2 posts", 2, posts.size());
        assertTrue(posts.contains(new Post("Test", "Dummy")));
        assertTrue(posts.contains(new Post("Highly", "Creative")));
        assertEquals("No tags!", blog.getName());
        assertEquals(0, blog.numbers.size());
    }

    @SuppressWarnings("unchecked")
    public void testLoadRootSet() {
        Yaml yaml = new Yaml();
        String doc = Util.getLocalResource("issues/issue73-3.txt");
        Set<String> strings = (Set<String>) yaml.load(doc);
        // System.out.println(strings);
        assertEquals(3, strings.size());
        assertEquals(HashSet.class, strings.getClass());
        assertTrue(strings.contains("aaa"));
        assertTrue(strings.contains("bbb"));
        assertTrue(strings.contains("ccc"));
    }

    @SuppressWarnings("unchecked")
    public void testLoadRootSet2() {
        Yaml yaml = new Yaml();
        String doc = "!!java.util.HashSet {aaa: null, bbb: null, ccc: null}";
        Set<String> strings = (Set<String>) yaml.load(doc);
        // System.out.println(strings);
        assertEquals(3, strings.size());
        assertEquals(LinkedHashSet.class, strings.getClass());
        assertTrue(strings.contains("aaa"));
        assertTrue(strings.contains("bbb"));
        assertTrue(strings.contains("ccc"));
    }

    @SuppressWarnings("unchecked")
    public void testLoadRootSet3() {
        Yaml yaml = new Yaml();
        String doc = "!!java.util.TreeSet {aaa: null, bbb: null, ccc: null}";
        Set<String> strings = (Set<String>) yaml.load(doc);
        // System.out.println(strings);
        assertEquals(3, strings.size());
        assertEquals(TreeSet.class, strings.getClass());
        assertTrue(strings.contains("aaa"));
        assertTrue(strings.contains("bbb"));
        assertTrue(strings.contains("ccc"));
    }

    @SuppressWarnings("unchecked")
    public void testLoadRootSet6() {
        Yaml yaml = new Yaml();
        String doc = Util.getLocalResource("issues/issue73-6.txt");
        Set<String> strings = (Set<String>) yaml.load(doc);
        // System.out.println(strings);
        assertEquals(3, strings.size());
        assertEquals(TreeSet.class, strings.getClass());
        assertTrue(strings.contains("aaa"));
        assertTrue(strings.contains("bbb"));
        assertTrue(strings.contains("ccc"));
    }
}
