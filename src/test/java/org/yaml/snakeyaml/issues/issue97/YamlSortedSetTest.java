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
package org.yaml.snakeyaml.issues.issue97;

import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.SequenceNode;

public class YamlSortedSetTest extends TestCase {
    public void testYaml() {
        String serialized = "!!org.yaml.snakeyaml.issues.issue97.Blog\n" + "posts:\n"
                + "  - text: Dummy\n" + "    title: Test\n" + "  - text: Creative\n"
                + "    title: Highly\n";
        // System.out.println(serialized);
        Yaml yaml2 = constructYamlParser();
        Blog rehydrated = (Blog) yaml2.load(serialized);
        checkTestBlog(rehydrated);
    }

    protected Yaml constructYamlParser() {
        Yaml yaml = new Yaml(new SetContructor());
        yaml.setBeanAccess(BeanAccess.FIELD);
        return yaml;
    }

    protected void checkTestBlog(Blog blog) {
        Set<Post> posts = blog.getPosts();
        Assert.assertEquals("Blog contains 2 posts", 2, posts.size());
    }

    private class SetContructor extends Constructor {
        public SetContructor() {
            yamlClassConstructors.put(NodeId.sequence, new ConstructSetFromSequence());
        }

        private class ConstructSetFromSequence extends ConstructSequence {
            @Override
            public Object construct(Node node) {
                if (SortedSet.class.isAssignableFrom(node.getType())) {
                    if (node.isTwoStepsConstruction()) {
                        throw new YAMLException("Set cannot be recursive.");
                    } else {
                        Collection<Object> result = new TreeSet<Object>();
                        SetContructor.this.constructSequenceStep2((SequenceNode) node, result);
                        return result;
                    }
                } else {
                    return super.construct(node);
                }
            }
        }
    }
}
