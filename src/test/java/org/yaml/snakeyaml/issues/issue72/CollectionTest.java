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

package org.yaml.snakeyaml.issues.issue72;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;

public class CollectionTest extends TestCase {

    public void testCollectionList() {
        CollectionList bean = new CollectionList();
        JavaBeanDumper dumper = new JavaBeanDumper();
        String doc = dumper.dump(bean);
        // System.out.println(doc);
        JavaBeanLoader<CollectionList> beanLoader = new JavaBeanLoader<CollectionList>(
                CollectionList.class);
        CollectionList parsed = beanLoader.load(doc);
        assertTrue(parsed.getNames().contains("aaa"));
        assertTrue(parsed.getNames().contains("bbb"));
        assertEquals(2, parsed.getNames().size());
    }

    public static class CollectionList {
        private Collection<String> names;

        public CollectionList() {
            names = new ArrayList<String>();
            names.add("aaa");
            names.add("bbb");
        }

        public Collection<String> getNames() {
            return names;
        }

        public void setNames(Collection<String> names) {
            this.names = names;
        }
    }

    public void testCollectionSet() {
        CollectionSet bean = new CollectionSet();
        JavaBeanDumper dumper = new JavaBeanDumper();
        String doc = dumper.dump(bean);
        // System.out.println(doc);
        JavaBeanLoader<CollectionSet> beanLoader = new JavaBeanLoader<CollectionSet>(
                CollectionSet.class);
        CollectionSet parsed = beanLoader.load(doc);
        assertTrue(parsed.getRoles().contains(11));
        assertTrue(parsed.getRoles().contains(13));
        assertEquals(2, parsed.getRoles().size());
    }

    public static class CollectionSet {
        private Collection<Integer> roles;

        public CollectionSet() {
            roles = new HashSet<Integer>();
            roles.add(11);
            roles.add(13);
        }

        public Collection<Integer> getRoles() {
            return roles;
        }

        public void setRoles(Collection<Integer> roles) {
            this.roles = roles;
        }
    }

}
