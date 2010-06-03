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

package org.yaml.snakeyaml.issues.issue61;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Util;

public class GenericListBeanTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testGenericList() throws Exception {
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        ListProvider<String> listProvider = new ListProvider<String>();
        listProvider.getList().add("foo");
        listProvider.getList().add("bar");
        String s = beanDumper.dump(listProvider);
        // System.out.println(s);
        assertEquals("list:\n- foo\n- bar\n", s);
        // parse
        JavaBeanLoader<ListProvider> loader = new JavaBeanLoader<ListProvider>(ListProvider.class);
        ListProvider<String> listProvider2 = loader.load(s);
        assertEquals("foo", listProvider2.getList().get(0));
        assertEquals("bar", listProvider2.getList().get(1));
        assertEquals(listProvider, listProvider2);
    }

    @SuppressWarnings("unchecked")
    public void testGenericBean() throws Exception {
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        ListProvider<Bean> listProvider = new ListProvider<Bean>();
        Bean foo = new Bean();
        foo.setName("foo");
        listProvider.getList().add(foo);
        Bean bar = new Bean();
        bar.setName("bar");
        bar.setNumber(3);
        listProvider.getList().add(bar);
        String s = beanDumper.dump(listProvider);
        // System.out.println(s);
        String etalon = Util.getLocalResource("issues/issue61-1.yaml");
        assertEquals(etalon, s);
        // parse
        JavaBeanLoader<ListProvider> loader = new JavaBeanLoader<ListProvider>(ListProvider.class);
        ListProvider listProvider2 = loader.load(s);
        Bean foo2 = (Bean) listProvider2.getList().get(0);
        assertEquals("foo", foo2.getName());
        assertEquals(0, foo2.getNumber());
        Bean bar2 = (Bean) listProvider2.getList().get(1);
        assertEquals("bar", bar2.getName());
        assertEquals(3, bar2.getNumber());
    }

    public static class ListProvider<T> {
        private List<T> list = new ArrayList<T>();

        public List<T> getList() {
            return list;
        }

        public void setList(List<T> list) {
            this.list = list;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ListProvider) {
                return list.equals(((ListProvider) obj).getList());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return list.hashCode();
        }
    }

    public static class Bean {
        private String name;
        private int number;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
