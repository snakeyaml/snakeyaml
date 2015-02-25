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
            // TODO improve the error message - the pointer should be at the
            // property name, not value
            assertTrue(e.getMessage().startsWith(
                    "Cannot create property=packageField for JavaBean=WrongJavaBean"));
            assertEquals(
                    "Unable to find property 'packageField' on class: org.yaml.snakeyaml.representer.WrongJavaBean",
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
            // TODO improve the error message - the pointer should be at the
            // property name, not value
            assertTrue(e.getMessage().startsWith(
                    "Cannot create property=staticField for JavaBean=WrongJavaBean"));
            assertEquals(
                    "Unable to find property 'staticField' on class: org.yaml.snakeyaml.representer.WrongJavaBean",
                    e.getCause().getMessage());
        }
    }
}
