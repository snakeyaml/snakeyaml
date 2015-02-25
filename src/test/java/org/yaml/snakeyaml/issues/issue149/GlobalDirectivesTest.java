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
package org.yaml.snakeyaml.issues.issue149;

import java.util.Iterator;

import junit.framework.TestCase;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

public class GlobalDirectivesTest extends TestCase {
    public void testOneDocument() {
        String input = Util.getLocalResource("issues/issue149-one-document.yaml");
        // System.out.println(input);
        Constructor constr = new Constructor();
        TypeDescription description = new TypeDescription(ComponentBean.class, new Tag(
                "tag:ualberta.ca,2012:29"));
        constr.addTypeDescription(description);
        Yaml yaml = new Yaml(constr);
        Iterator<Object> parsed = yaml.loadAll(input).iterator();
        ComponentBean bean = (ComponentBean) parsed.next();
        assertEquals(0, bean.getProperty1());
        assertEquals("aaa", bean.getProperty2());
        assertFalse(parsed.hasNext());
    }

    public void testDirectives() {
        String input = Util.getLocalResource("issues/issue149-losing-directives.yaml");
        // System.out.println(input);
        Constructor constr = new Constructor();
        TypeDescription description = new TypeDescription(ComponentBean.class, new Tag(
                "tag:ualberta.ca,2012:" + 29));
        constr.addTypeDescription(description);
        Yaml yaml = new Yaml(constr);
        Iterator<Object> parsed = yaml.loadAll(input).iterator();
        ComponentBean bean1 = (ComponentBean) parsed.next();
        assertEquals(0, bean1.getProperty1());
        assertEquals("aaa", bean1.getProperty2());
        ComponentBean bean2 = (ComponentBean) parsed.next();
        assertEquals(3, bean2.getProperty1());
        assertEquals("bbb", bean2.getProperty2());
        assertFalse(parsed.hasNext());
    }

    public void testDirectives2() {
        String input = Util.getLocalResource("issues/issue149-losing-directives-2.yaml");
        // System.out.println(input);
        Constructor constr = new Constructor();
        TypeDescription description = new TypeDescription(ComponentBean.class, new Tag(
                "tag:ualberta.ca,2012:" + 29));
        constr.addTypeDescription(description);
        Yaml yaml = new Yaml(constr);
        Iterator<Object> parsed = yaml.loadAll(input).iterator();
        ComponentBean bean1 = (ComponentBean) parsed.next();
        assertEquals(0, bean1.getProperty1());
        assertEquals("aaa", bean1.getProperty2());
        ComponentBean bean2 = (ComponentBean) parsed.next();
        assertEquals(3, bean2.getProperty1());
        assertEquals("bbb", bean2.getProperty2());
        assertFalse(parsed.hasNext());
    }
}