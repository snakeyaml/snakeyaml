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
package org.yaml.snakeyaml.issues.issue74;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class ArrayBeanTest extends TestCase {

    public void testArrayProperty() {
        ArrayMember[] members = new ArrayMember[3];
        members[0] = new ArrayMember("Foo", 21);
        members[1] = new ArrayMember("Bar", 23);
        members[2] = new ArrayMember("Hue Long Hair", 25);
        ArrayBean bean = new ArrayBean();
        bean.setId("ID123");
        bean.setNumber(7);
        bean.setMembers(members);
        bean.openMembers = new ArrayMember[] { new ArrayMember("OpenFoo", 1000),
                new ArrayMember("OpenBar", 2000) };
        List<ArrayMember> list = new ArrayList<ArrayMember>(2);
        list.add(new ArrayMember("John", 111));
        list.add(new ArrayMember("Tony", 222));
        bean.setList(list);
        Yaml yaml = new Yaml();
        String output = yaml.dumpAsMap(bean);
        // System.out.println(output);
        assertEquals(Util.getLocalResource("issues/issue74-array1.txt"), output);
        Yaml beanLoader = new Yaml();
        ArrayBean parsed = beanLoader.loadAs(output, ArrayBean.class);
        // System.out.println(parsed);
        assertEquals(3, parsed.getMembers().length);
        assertEquals(2, parsed.openMembers.length);
        assertEquals(2, parsed.getList().size());
        assertEquals("ID123", parsed.getId());
        assertEquals(7, parsed.getNumber());
        for (ArrayMember member : parsed.getMembers()) {
            assertTrue((member.getAge() >= 21) && (member.getAge() <= 25));
        }
    }

    public static class ArrayBean {
        private String id;
        private int number;
        private ArrayMember[] members;
        public ArrayMember[] openMembers;
        private List<ArrayMember> list;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public ArrayMember[] getMembers() {
            return members;
        }

        public void setMembers(ArrayMember[] members) {
            this.members = members;
        }

        public List<ArrayMember> getList() {
            return list;
        }

        public void setList(List<ArrayMember> list) {
            this.list = list;
        }
    }

    public static class ArrayMember {
        private String name;
        private int age;

        public ArrayMember(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public ArrayMember() {
            this.name = "NoName";
            this.age = 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof ArrayMember) {
                ArrayMember m = (ArrayMember) obj;
                return age == m.age;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return age;
        }

        @Override
        public String toString() {
            return "ArrayMember age=" + age;
        }
    }
}
