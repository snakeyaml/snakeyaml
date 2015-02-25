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
package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.error.YAMLException;

public class CompactConstructorErrorsTest extends TestCase {

    public void test1() {
        BaseConstructor compact = new CompactConstructor();
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/error1.yaml");
        try {
            yaml.load(doc);
            fail("Package is not specified.");
        } catch (Exception e) {
            assertEquals("java.lang.ClassNotFoundException: Table", e.getMessage());
        }
    }

    private Object load(String fileName) {
        CompactConstructor compact = new PackageCompactConstructor(
                "org.yaml.snakeyaml.extensions.compactnotation");
        Yaml yaml = new Yaml(compact);
        String doc = Util.getLocalResource("compactnotation/" + fileName);
        Object obj = yaml.load(doc);
        assertNotNull(obj);
        return obj;
    }

    private void failLoad(String fileName, String failure) {
        load(fileName);
        fail(failure);
    }

    private void check(String fileName, String failure, String message) {
        check(fileName, failure, message, true);
    }

    private void check(String fileName, String failure, String message, boolean exactMatch) {
        try {
            failLoad(fileName, failure);
        } catch (YAMLException e) {
            String eMessage = e.getMessage();
            if (exactMatch) {
                assertEquals(message, eMessage);
            } else {
                assertNotNull("Exception message is NULL", eMessage);
                assertTrue(String.format(
                        "\nException message\n%s\ndoes not contain expected value\n%s",
                        e.getMessage(), message), eMessage.contains(message));
            }
        } catch (Exception e) {
            fail("Exception must be YAMLException");
        }
    }

    public void test2() {
        check("error2.yaml",
                "No single argument constructor provided.",
                "java.lang.NoSuchMethodException: org.yaml.snakeyaml.extensions.compactnotation.Table.<init>(java.lang.String)");
    }

    public void test3() {
        check("error3.yaml",
                "In-line parameters can only be Strings.",
                "org.yaml.snakeyaml.error.YAMLException: Cannot set property='size' with value='17' (class java.lang.String) in Row id=id111");
    }

    /**
     * Created Map instead of Row
     */
    @SuppressWarnings("unchecked")
    public void test4() {
        Table table = (Table) load("error4.yaml");
        List<Row> rows = table.getRows();
        assertEquals(1, rows.size());
        assertFalse("Row should not be created.", rows.get(0) instanceof Row);
        Map<String, String> map = (Map<String, String>) rows.get(0);
        assertEquals(1, map.size());
        assertEquals("15}", map.get("Row(id111, description = text) {size"));
    }

    /**
     * Wrong indent
     */
    @SuppressWarnings("unchecked")
    public void test5() {
        Table table = (Table) load("error5.yaml");
        List<Row> rows = table.getRows();
        assertEquals(1, rows.size());
        assertFalse("Row should not be created.", rows.get(0) instanceof Row);
        Map<String, String> map = (Map<String, String>) rows.get(0);
        assertEquals(4, map.size());
        // System.out.println(map);
        assertNull(map.get(new Row("id222")));
        assertTrue(map.containsKey(new Row("id222")));
        assertEquals(17, map.get("size"));
    }

    public void test6() {
        check("error6.yaml",
                "Invalid property.",
                "org.yaml.snakeyaml.error.YAMLException: Unable to find property 'foo' on class: org.yaml.snakeyaml.extensions.compactnotation.Table");
    }

    public void test7() {
        check("error7.yaml",
                "Invalid property.",
                "Unable to find property 'foo' on class: org.yaml.snakeyaml.extensions.compactnotation.Table",
                false);
    }

    public void test8() {
        check("error8.yaml",
                "No list property",
                "org.yaml.snakeyaml.error.YAMLException: No list property found in class org.yaml.snakeyaml.extensions.compactnotation.Row");
    }

    public void test9() {
        check("error9.yaml",
                "Many list properties found",
                "org.yaml.snakeyaml.error.YAMLException: Many list properties found in class org.yaml.snakeyaml.extensions.compactnotation.ManyListsTable; Please override getSequencePropertyName() to specify which property to use.");
    }
}
