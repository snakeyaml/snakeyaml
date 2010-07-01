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

package org.yaml.snakeyaml.issues.issue55;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Loader;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class YamlFieldAccessCollectionTest {

    @Test
    public void testYaml() {

        Blog original = createTestBlog();
        Yaml yamlDumper = constructYamlDumper();

        String serialized = yamlDumper.dump(original);
        // System.out.println(serialized);
        Assert.assertEquals(Util.getLocalResource("issues/issue55_1.txt"), serialized);
        JavaBeanLoader<Blog> blogLoader = new JavaBeanLoader<Blog>(Blog.class, BeanAccess.FIELD);
        Blog rehydrated = (Blog) blogLoader.load(serialized);
        checkTestBlog(rehydrated);
    }

    /*
     * @Test public void testYaml() {
     * 
     * Blog original = createTestBlog(); Yaml yamlDumper =
     * constructYamlDumper();
     * 
     * String serialized = yamlDumper.dump(original); //
     * System.out.println(serialized);
     * Assert.assertEquals(Util.getLocalResource("issues/issue55_1.txt"),
     * serialized); Yaml yaml2 = constructYamlParser(); // this fails with
     * "No constructor with 2 arguments found..." Blog rehydrated; try {
     * rehydrated = (Blog) yaml2.load(serialized); Assert.fail("Fix issue 55");
     * checkTestBlog(rehydrated); } catch (Exception e) { Thread.dumpStack();
     * Assert.assertTrue(true);// FIXME issue 55 } }
     */

    protected Yaml constructYamlDumper() {

        Representer representer = new Representer();
        representer.getPropertyUtils().setBeanAccess(BeanAccess.FIELD);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        options.setExplicitRoot(Tag.MAP);
        Dumper dumper = new Dumper(representer, options);

        Yaml yaml = new Yaml(dumper);
        return yaml;

    }

    protected Yaml constructYamlParser() {

        Loader loader = new Loader();
        loader.setBeanAccess(BeanAccess.FIELD);

        Yaml yaml = new Yaml(loader);
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
        Assert.assertEquals("Blog contains 2 posts", 2, posts.size());
    }
}
