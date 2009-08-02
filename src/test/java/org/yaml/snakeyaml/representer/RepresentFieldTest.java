/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.representer;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Yaml;

public class RepresentFieldTest extends TestCase {

    public void testRepresent1() {
        Yaml yaml = new Yaml();
        WrongJavaBean bean = new WrongJavaBean();
        bean.packageField = "Value";// the field is present
        bean.publicField = "Michael Jackson";
        WrongJavaBean.staticField = "Another value";
        String output = yaml.dump(bean);
        assertEquals(
                "!!org.yaml.snakeyaml.representer.WrongJavaBean {publicField: Michael Jackson}\n",
                output);
    }

    public void testWrongNotPublicField() {
        Yaml yaml = new Yaml();
        WrongJavaBean bean = new WrongJavaBean();
        bean.packageField = "Value";// the field is present
        try {
            yaml.load("!!org.yaml.snakeyaml.representer.WrongJavaBean {packageField: Gnome}\n");
            fail("Only public fields can be used.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.representer.WrongJavaBean; exception=Cannot create property=packageField for JavaBean=WrongJavaBean; Unable to find property 'packageField' on class: org.yaml.snakeyaml.representer.WrongJavaBean",
                    e.getMessage());
            assertEquals(
                    "Cannot create property=packageField for JavaBean=WrongJavaBean; Unable to find property 'packageField' on class: org.yaml.snakeyaml.representer.WrongJavaBean",
                    e.getCause().getMessage());
        }
    }

    public void testStaticField() {
        Yaml yaml = new Yaml();
        WrongJavaBean.staticField = "Value";// the field is present
        try {
            yaml.load("!!org.yaml.snakeyaml.representer.WrongJavaBean {staticField: Gnome}\n");
            fail("Static fields cannot be used.");
        } catch (Exception e) {
            assertEquals(
                    "null; Can't construct a java object for tag:yaml.org,2002:org.yaml.snakeyaml.representer.WrongJavaBean; exception=Cannot create property=staticField for JavaBean=WrongJavaBean; Unable to find property 'staticField' on class: org.yaml.snakeyaml.representer.WrongJavaBean",
                    e.getMessage());
            assertEquals(
                    "Cannot create property=staticField for JavaBean=WrongJavaBean; Unable to find property 'staticField' on class: org.yaml.snakeyaml.representer.WrongJavaBean",
                    e.getCause().getMessage());
        }
    }
}
