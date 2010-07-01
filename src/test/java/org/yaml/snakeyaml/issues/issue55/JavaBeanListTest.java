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

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class JavaBeanListTest {

    @Test
    public void testYaml() {
        JavaBeanLoader<BlogBean> beanLoader = new JavaBeanLoader<BlogBean>(BlogBean.class,
                BeanAccess.FIELD);
        BlogBean rehydrated = (BlogBean) beanLoader.load(Util
                .getLocalResource("issues/issue55_2.txt"));
        Assert.assertEquals(4, rehydrated.getPosts().size());
    }

    @Test
    public void testFailureWithoutFieldAccess() {
        JavaBeanLoader<BlogBean> beanLoader = new JavaBeanLoader<BlogBean>(BlogBean.class);
        try {
            beanLoader.load(Util.getLocalResource("issues/issue55_2.txt"));
            Assert.fail("Private field must not be available");
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage().contains("Unable to find property 'posts'"));
        }
    }

    public static class BlogBean {
        private List<Integer> posts;

        public BlogBean() {
            posts = new LinkedList<Integer>();
        }

        public List<Integer> getPosts() {
            return posts;
        }
    }
}
