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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

class Blog {

    private String name;
    private Set<Post> posts = new HashSet<Post>();
    public Set<Integer> numbers = new TreeSet<Integer>();

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
}