/**
 * Copyright (c) 2008-2011, http://www.snakeyaml.org
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

package org.yaml.snakeyaml.javabeans;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.Yaml;

public class TriangleBeanTest extends TestCase {

    public void testGetTriangle() {
        Triangle triangle = new Triangle();
        triangle.setName("Triangle25");
        TriangleBean bean = new TriangleBean();
        bean.setShape(triangle);
        bean.setName("Bean25");
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        String output = beanDumper.dump(bean);
        assertEquals(
                "name: Bean25\nshape: !!org.yaml.snakeyaml.javabeans.Triangle\n  name: Triangle25\n",
                output);
        Yaml beanLoader = new Yaml();
        TriangleBean loadedBean = beanLoader.loadAs(output, TriangleBean.class);
        assertNotNull(loadedBean);
        assertEquals("Bean25", loadedBean.getName());
        assertEquals(7, loadedBean.getShape().process());
    }

    public void testClassNotFound() {
        String output = "name: Bean25\nshape: !!org.yaml.snakeyaml.javabeans.Triangle777\n  name: Triangle25\n";
        Yaml beanLoader = new Yaml();
        try {
            beanLoader.loadAs(output, TriangleBean.class);
            fail("Class not found expected.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.javabeans.TriangleBean; exception=Cannot create property=shape for JavaBean=TriangleBean name=Bean25; null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.javabeans.Triangle777; exception=Class not found: org.yaml.snakeyaml.javabeans.Triangle777",
                    e.getMessage());
        }
    }

    /**
     * Runtime class has less priority then an explicit tag
     */
    public void testClassAndTag() {
        String output = "name: !!whatever Bean25\nshape: !!org.yaml.snakeyaml.javabeans.Triangle\n  name: Triangle25\n";
        Yaml beanLoader = new Yaml();
        try {
            beanLoader.loadAs(output, TriangleBean.class);
            fail("Runtime class has less priority then an explicit tag");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.javabeans.TriangleBean; exception=Cannot create property=name for JavaBean=TriangleBean name=null; null; Can't construct a java object for tag:yaml.org,2002:whatever; exception=Class not found: whatever",
                    e.getMessage());
        }
    }
}
