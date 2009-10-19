/**
 * Copyright (c) 2008-2009 Andrey Somov
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
package examples;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.Util;

public class TypeSafeListTest extends TestCase {
    public void qtestDumpList() {
        ListBean bean = new ListBean();
        List<String> list = new ArrayList<String>();
        list.add("aaa");
        list.add("bbb");
        bean.setChildren(list);
        List<Developer> developers = new ArrayList<Developer>();
        developers.add(new Developer("Fred", "creator"));
        developers.add(new Developer("John", "committer"));
        bean.setDevelopers(developers);
        JavaBeanDumper dumper = new JavaBeanDumper(false);
        String output = dumper.dump(bean);
        System.out.println(output);
        String etalon = Util.getLocalResource("examples/list-bean-1.yaml");
        // TODO dump type safe collections
        // assertEquals(etalon, output);
    }

    public void testLoadList() {
        String output = Util.getLocalResource("examples/list-bean-1.yaml");
        // System.out.println(output);
        JavaBeanLoader<ListBean> beanLoader = new JavaBeanLoader<ListBean>(ListBean.class);
        ListBean parsed = beanLoader.load(output);
        assertNotNull(parsed);
        List<String> list2 = parsed.getChildren();
        assertEquals(2, list2.size());
        assertEquals("aaa", list2.get(0));
        assertEquals("bbb", list2.get(1));
        List<Developer> developers = parsed.getDevelopers();
        assertEquals(2, developers.size());
        assertEquals("Developer must be recognised.", Developer.class, developers.get(0).getClass());
        Developer fred = developers.get(0);
        assertEquals("Fred", fred.getName());
        assertEquals("creator", fred.getRole());
    }
}
