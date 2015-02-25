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

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class Blog {

    private String name;
    private Set<Post> posts = new TreeSet<Post>();
    public Set<Integer> numbers = new LinkedHashSet<Integer>();
    private TreeSet<String> labels = new TreeSet<String>();

    public Blog() {
        name = "SuperBlog";
    }

    public Blog(String name) {
        this.name = name;
    }

    public void addPost(Post p) {
        posts.add(p);
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public TreeSet<String> getLabels() {
        return labels;
    }

    public void setLabels(TreeSet<String> labels) {
        this.labels = labels;
    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Blog '" + name + "'";
    }
}