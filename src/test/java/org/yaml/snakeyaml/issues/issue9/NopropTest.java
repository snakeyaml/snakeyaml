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
package org.yaml.snakeyaml.issues.issue9;

import java.util.Iterator;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class NopropTest extends TestCase {

    public void testOK01() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean1\n  intVal : 11\n  strVal : HALLO_1 ";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("HALLO_1", beanHolder.getBean().getStrVal());
        assertEquals(11, beanHolder.getBean().getIntVal());
    }

    public void testOK02() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean2\n  intVal : 22\n  strVal : HALLO_2 ";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("HALLO_2", beanHolder.getBean().getStrVal());
        assertEquals(22, beanHolder.getBean().getIntVal());
    }

    public void testOK03() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean1\n  intVal : 1";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(1, beanHolder.getBean().getIntVal());
    }

    public void testOK04() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean2\n  intVal : 22";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_2", beanHolder.getBean().getStrVal());
        assertEquals(22, beanHolder.getBean().getIntVal());
    }

    public void testOK05() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean1\n  strVal : HALLO_1 ";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("HALLO_1", beanHolder.getBean().getStrVal());
        assertEquals(1, beanHolder.getBean().getIntVal());
    }

    public void testOK06() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean2\n  strVal : HALLO_2 ";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("HALLO_2", beanHolder.getBean().getStrVal());
        assertEquals(2, beanHolder.getBean().getIntVal());
    }

    public void testEmptyBean() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean1";
        Iterator<Object> docs = new Yaml(new BeanConstructor()).loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(1, beanHolder.getBean().getIntVal());
    }

    public void testEmptyBean2() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean1 {}";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(1, beanHolder.getBean().getIntVal());
    }

    public void testEmptyDoc() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder";
        Iterator<Object> docs = new Yaml(new BeanConstructor()).loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(3, beanHolder.getBean().getIntVal());
    }

    public void testEmptyDoc2() {
        String yaml = "---";
        Iterator<Object> docs = new Yaml(new BeanConstructor()).loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertNotNull(beanHolder);
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(3, beanHolder.getBean().getIntVal());
        // only space is also null
        yaml = "--- ";
        docs = new Yaml(new BeanConstructor()).loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        beanHolder = (BeanHolder) docs.next();
        assertNotNull(beanHolder);
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(3, beanHolder.getBean().getIntVal());
        // only space is also null
        yaml = "--- '23'";
        docs = new Yaml(new BeanConstructor()).loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        beanHolder = (BeanHolder) docs.next();
        assertNotNull(beanHolder);
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(23, beanHolder.getBean().getIntVal());
    }

    public void testEmptyDoc3() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder";
        Iterator<Object> docs = new Yaml(new BeanConstructor3()).loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(1, beanHolder.getBean().getIntVal());
    }

    public void testNonBean() {
        String yaml = "--- !!org.yaml.snakeyaml.issues.issue9.BeanHolder\nbean : !!org.yaml.snakeyaml.issues.issue9.Bean1 123";
        Iterator<Object> docs = new Yaml().loadAll(yaml).iterator();
        assertTrue(docs.hasNext());
        BeanHolder beanHolder = (BeanHolder) docs.next();
        assertEquals("BEAN_1", beanHolder.getBean().getStrVal());
        assertEquals(123, beanHolder.getBean().getIntVal());
    }
}
