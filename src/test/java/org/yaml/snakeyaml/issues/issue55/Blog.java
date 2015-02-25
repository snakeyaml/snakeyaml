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

class Blog {
    private List<Post> posts = new LinkedList<Post>();

    public Blog() {
    }

    public void addPost(Post p) {
        // do some business logic here
        posts.add(p);
    }

    public List<Post> getPosts() {
        // in production code do not return the original set but a wrapped
        // unmodifiable set
        return posts;
    }
}