/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.javabeans;

import junit.framework.TestCase;

import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.JavaBeanLoader;

public class TriangleBeanTest extends TestCase {

    public void testGetTriangle() {
        Triangle triangle = new Triangle();
        triangle.setName("Triangle25");
        TriangleBean bean = new TriangleBean();
        bean.setShape(triangle);
        bean.setName("Bean25");
        JavaBeanDumper beanDumper = new JavaBeanDumper();
        String output = beanDumper.dump(bean);
        System.out.println(output);
        assertEquals(
                "name: Bean25\nshape: !!org.yaml.snakeyaml.javabeans.Triangle\n  name: Triangle25\n",
                output);
        JavaBeanLoader<TriangleBean> beanLoader = new JavaBeanLoader<TriangleBean>(
                TriangleBean.class);
        TriangleBean loadedBean = beanLoader.load(output);
        assertNotNull(loadedBean);
        assertEquals("Bean25", loadedBean.getName());
        assertEquals(7, loadedBean.getShape().process());
    }

    public void testClassNotFound() {
        String output = "name: Bean25\nshape: !!org.yaml.snakeyaml.javabeans.Triangle777\n  name: Triangle25\n";
        JavaBeanLoader<TriangleBean> beanLoader = new JavaBeanLoader<TriangleBean>(
                TriangleBean.class);
        try {
            beanLoader.load(output);
            fail("Class not found expected.");
        } catch (Exception e) {
            assertEquals(
                    "Cannot create property=shape for JavaBean=TriangleBean name=Bean25; java.lang.ClassNotFoundException: org.yaml.snakeyaml.javabeans.Triangle777",
                    e.getMessage());
        }
    }

    /**
     * Runtime class is more important then the tag (which is ignored if the
     * class is known)
     */
    public void testClassAndTag() {
        String output = "name: !!whatever Bean25\nshape: !!org.yaml.snakeyaml.javabeans.Triangle\n  name: Triangle25\n";
        JavaBeanLoader<TriangleBean> beanLoader = new JavaBeanLoader<TriangleBean>(
                TriangleBean.class);
        TriangleBean loadedBean = beanLoader.load(output);
        assertNotNull(loadedBean);
        assertEquals("Bean25", loadedBean.getName());
        assertEquals(7, loadedBean.getShape().process());
    }
}
