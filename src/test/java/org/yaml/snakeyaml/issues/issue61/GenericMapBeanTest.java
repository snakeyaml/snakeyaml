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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.LoaderOptions.ImplicitMode;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class GenericMapBeanTest extends TestCase {
    @SuppressWarnings("unchecked")
    public void testGenericMap() throws Exception {
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        MapProvider<String, Integer> listProvider = new MapProvider<String, Integer>();
        listProvider.getMap().put("foo", 17);
        listProvider.getMap().put("bar", 19);
        String s = beanDumper.dump(listProvider);
        // System.out.println(s);
        assertEquals("map:\n  foo: 17\n  bar: 19\n", s);
        // parse
        LoaderOptions options = new LoaderOptions();
        options.setRootTypeDescription(new TypeDescription(MapProvider.class));
        options.setImplicitMode(ImplicitMode.ALWAYS_IMPLICIT_TYPES);
        JavaBeanLoader<MapProvider> loader = new JavaBeanLoader<MapProvider>(options,
                BeanAccess.DEFAULT);
        MapProvider<String, Integer> listProvider2 = loader.load(s);
        assertEquals(new Integer(17), listProvider2.getMap().get("foo"));
        assertEquals(new Integer(19), listProvider2.getMap().get("bar"));
        assertEquals(listProvider, listProvider2);
    }

    @SuppressWarnings("unchecked")
    public void testGenericMapNoImplicitTypes() throws Exception {
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        MapProvider<String, Integer> listProvider = new MapProvider<String, Integer>();
        listProvider.getMap().put("foo", 17);
        listProvider.getMap().put("bar", 19);
        String s = beanDumper.dump(listProvider);
        // System.out.println(s);
        assertEquals("map:\n  foo: 17\n  bar: 19\n", s);
        // parse
        LoaderOptions options = new LoaderOptions();
        options.setRootTypeDescription(new TypeDescription(MapProvider.class));
        options.setImplicitMode(ImplicitMode.NEVER_IMPLICIT_TYPES);
        JavaBeanLoader<MapProvider> loader = new JavaBeanLoader<MapProvider>(options,
                BeanAccess.DEFAULT);
        MapProvider<String, Integer> listProvider2 = loader.load(s);
        assertEquals("17", listProvider2.getMap().get("foo"));
        assertEquals("19", listProvider2.getMap().get("bar"));
        assertFalse(listProvider.equals(listProvider2));
    }

    @SuppressWarnings("unchecked")
    public void testGenericBean() throws Exception {
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        MapProvider<String, Bean> listProvider = new MapProvider<String, Bean>();
        Bean foo = new Bean();
        foo.setName("foo");
        listProvider.getMap().put("foo", foo);
        Bean bar = new Bean();
        bar.setName("bar");
        bar.setNumber(3);
        listProvider.getMap().put("bar", bar);
        String s = beanDumper.dump(listProvider);
        // System.out.println(s);
        String etalon = Util.getLocalResource("issues/issue61-2.yaml");
        assertEquals(etalon, s);
        // parse
        JavaBeanLoader<MapProvider> loader = new JavaBeanLoader<MapProvider>(MapProvider.class);
        MapProvider listProvider2 = loader.load(s);
        Bean foo2 = (Bean) listProvider2.getMap().get("foo");
        assertEquals("foo", foo2.getName());
        assertEquals(0, foo2.getNumber());
        Bean bar2 = (Bean) listProvider2.getMap().get("bar");
        assertEquals("bar", bar2.getName());
        assertEquals(3, bar2.getNumber());
    }

    public static class MapProvider<K, V> {
        private Map<K, V> map = new HashMap<K, V>();

        public Map<K, V> getMap() {
            return map;
        }

        public void setMap(Map<K, V> map) {
            this.map = map;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MapProvider) {
                return map.equals(((MapProvider) obj).getMap());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return map.hashCode();
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
