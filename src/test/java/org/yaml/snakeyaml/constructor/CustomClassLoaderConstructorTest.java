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
package org.yaml.snakeyaml.constructor;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class CustomClassLoaderConstructorTest extends TestCase {

    public void testGetClassForNameNull() {
        try {
            new CustomClassLoaderConstructor(null);
            fail();
        } catch (Exception e) {
            assertEquals("Loader must be provided.", e.getMessage());
        }
    }

    public void testGetClassForName() {
        CustomClassLoaderConstructor constr = new CustomClassLoaderConstructor(
                CustomClassLoaderConstructorTest.class.getClassLoader());
        Yaml yaml = new Yaml(constr);
        String s = (String) yaml.load("abc");
        assertEquals("abc", s);
    }

    public void testGetClassForNameWithRoot() throws ClassNotFoundException {
        Class<?> clazz = Class.forName(
                "org.yaml.snakeyaml.constructor.CustomClassLoaderConstructorTest$LoaderBean", true,
                CustomClassLoaderConstructorTest.class.getClassLoader());
        CustomClassLoaderConstructor constr = new CustomClassLoaderConstructor(clazz,
                CustomClassLoaderConstructorTest.class.getClassLoader());
        Yaml yaml = new Yaml(constr);
        LoaderBean bean = (LoaderBean) yaml.load("{name: Andrey, number: 555}");
        assertEquals("Andrey", bean.getName());
        assertEquals(555, bean.getNumber());
    }

    public void testGetClassForNameBean() {
        CustomClassLoaderConstructor constr = new CustomClassLoaderConstructor(
                CustomClassLoaderConstructorTest.class.getClassLoader());
        Yaml yaml = new Yaml(constr);
        LoaderBean bean = (LoaderBean) yaml
                .load("!!org.yaml.snakeyaml.constructor.CustomClassLoaderConstructorTest$LoaderBean {name: Andrey, number: 555}");
        assertEquals("Andrey", bean.getName());
        assertEquals(555, bean.getNumber());
    }

    public static class LoaderBean {
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
