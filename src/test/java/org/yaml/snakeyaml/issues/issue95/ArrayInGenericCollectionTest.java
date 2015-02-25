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
package org.yaml.snakeyaml.issues.issue95;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Assert;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;

public class ArrayInGenericCollectionTest extends TestCase {

    public static class A {
        private Map<String, String[]> meta = new HashMap<String, String[]>();
    }

    public static class B {
        private List<String[]> meta = new ArrayList<String[]>();
    }

    private A createA() {
        A a = new A();
        a.meta.put("met1", new String[] { "whatever" });
        a.meta.put("met2", new String[] { "something", "something else" });
        return a;
    }

    private B createB() {
        B b = new B();
        b.meta.add(new String[] { "whatever" });
        b.meta.add(new String[] { "something", "something else" });
        return b;
    }

    public void testArrayAsMapValue() {
        Yaml yaml2dump = new Yaml();
        yaml2dump.setBeanAccess(BeanAccess.FIELD);
        A data = createA();
        String dump = yaml2dump.dump(data);
        // System.out.println(dump);

        Yaml yaml2load = new Yaml();
        yaml2load.setBeanAccess(BeanAccess.FIELD);
        A loaded = (A) yaml2load.load(dump);

        assertEquals(data.meta.size(), loaded.meta.size());
        Set<Entry<String, String[]>> loadedMeta = loaded.meta.entrySet();
        for (Entry<String, String[]> entry : loadedMeta) {
            assertTrue(data.meta.containsKey(entry.getKey()));
            Assert.assertArrayEquals(data.meta.get(entry.getKey()), entry.getValue());
        }
    }

    public void testArrayAsMapValueWithTypeDespriptor() {
        Yaml yaml2dump = new Yaml();
        yaml2dump.setBeanAccess(BeanAccess.FIELD);
        A data = createA();
        String dump = yaml2dump.dump(data);
        // System.out.println(dump);

        TypeDescription aTypeDescr = new TypeDescription(A.class);
        aTypeDescr.putMapPropertyType("meta", String.class, String[].class);

        Constructor c = new Constructor();
        c.addTypeDescription(aTypeDescr);
        Yaml yaml2load = new Yaml(c);
        yaml2load.setBeanAccess(BeanAccess.FIELD);

        A loaded = (A) yaml2load.load(dump);

        assertEquals(data.meta.size(), loaded.meta.size());
        Set<Entry<String, String[]>> loadedMeta = loaded.meta.entrySet();
        for (Entry<String, String[]> entry : loadedMeta) {
            assertTrue(data.meta.containsKey(entry.getKey()));
            Assert.assertArrayEquals(data.meta.get(entry.getKey()), entry.getValue());
        }
    }

    public void testArrayAsListValue() {
        Yaml yaml2dump = new Yaml();
        yaml2dump.setBeanAccess(BeanAccess.FIELD);
        B data = createB();
        String dump = yaml2dump.dump(data);
        // System.out.println(dump);

        Yaml yaml2load = new Yaml();
        yaml2load.setBeanAccess(BeanAccess.FIELD);
        B loaded = (B) yaml2load.load(dump);

        Assert.assertArrayEquals(data.meta.toArray(), loaded.meta.toArray());
    }

    public void testArrayAsListValueWithTypeDespriptor() {
        Yaml yaml2dump = new Yaml();
        yaml2dump.setBeanAccess(BeanAccess.FIELD);
        B data = createB();
        String dump = yaml2dump.dump(data);
        // System.out.println(dump);

        TypeDescription aTypeDescr = new TypeDescription(B.class);
        aTypeDescr.putListPropertyType("meta", String[].class);

        Constructor c = new Constructor();
        c.addTypeDescription(aTypeDescr);
        Yaml yaml2load = new Yaml(c);
        yaml2load.setBeanAccess(BeanAccess.FIELD);

        B loaded = (B) yaml2load.load(dump);

        Assert.assertArrayEquals(data.meta.toArray(), loaded.meta.toArray());
    }

    public void testNoTags() {
        Yaml yaml2dump = new Yaml();
        yaml2dump.setBeanAccess(BeanAccess.FIELD);
        B data = createB();
        String dump = yaml2dump.dumpAs(data, Tag.MAP, FlowStyle.AUTO);
        // System.out.println(dump);
        assertEquals("meta:\n- [whatever]\n- [something, something else]\n", dump);
        //
        Constructor constr = new Constructor(B.class);
        Yaml yaml2load = new Yaml(constr);
        yaml2load.setBeanAccess(BeanAccess.FIELD);
        B loaded = (B) yaml2load.load(dump);

        Assert.assertArrayEquals(data.meta.toArray(), loaded.meta.toArray());
    }
}
