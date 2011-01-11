/**
 * Copyright (c) 2008-2010, http://www.snakeyaml.org
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

package org.yaml.snakeyaml.issues.issue100;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;

public class MergeJavaBeanTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testNoMerge() throws IOException {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- *id001";
        // System.out.println(input);
        Yaml yaml = new Yaml();
        List<Data> list = (List<Data>) yaml.load(input);
        for (Data data : list) {
            // System.out.println(data);
            assertEquals("id123", data.getId());
            assertEquals(11, data.getAge());
        }
    }

    @SuppressWarnings("unchecked")
    public void testMergeWithTags() throws IOException {
        String input = Util.getLocalResource("issues/issue100-1.yaml");
        //System.out.println(input);
        Yaml yaml = new Yaml();
        List<Data> list = (List<Data>) yaml.load(input);
        // First object: Data ( 11, "id123" )
        assertEquals(11, list.get(0).getAge());
        assertEquals("id123", list.get(0).getId());

        // Second object: Data ( 13, "id456" )
        assertEquals(13, list.get(1).getAge());
        assertEquals("id456", list.get(1).getId());

        // Third object: Data ( 11, "id789" )
        assertEquals(11, list.get(2).getAge());
        assertEquals("id789", list.get(2).getId());
    }

    @SuppressWarnings("unchecked")
    public void testMergeToIdenticalInstances() throws IOException {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- << : *id001";
        System.out.println(input);
        Yaml yaml = new Yaml();
        try {
            List<Data> list = (List<Data>) yaml.load(input);
            Data identity = list.iterator().next();
            for (Data data : list) {
                // System.out.println(data);
                assertEquals("id123", data.getId());
                assertEquals(11, data.getAge());
                assertTrue(identity == data);
            }
            fail("issue 100");
        } catch (Exception e) {
            // TODO issue 100
            // System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void testMergeAndDeviate() throws IOException {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- <<: *id001\n  id: id456";
        System.out.println(input);
        Yaml yaml = new Yaml();
        Set<String> ids = new HashSet<String>();
        try {
            List<Data> list = (List<Data>) yaml.load(input);
            for (Data data : list) {
                // System.out.println(data);
                assertEquals(11, data.getAge());
                ids.add(data.getId());
            }
            assertEquals("IDs must be different", 2, ids.size());
            fail("issue 100");
        } catch (Exception e) {
            // TODO issue 100
            System.out.println(e.getMessage());
        }
    }

    /**
     * <pre>
     * Test that the merge-override works correctly in the case of custom classes / data types.
     * Two base objects are specified:
     *    id001 refers to Data ( 11, "id123" )
     *    id002 refers to Data ( 37, null )  with a literal null value
     * The third object is specified as having the properties of id001, with the properties of
     * id002 overriding where they conflict.
     *    Data ( 37, "id123" )
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public void testMergeAndDeviateOverride() throws IOException {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- &id002 !!org.yaml.snakeyaml.issues.issue100.Data {age: 37}\n- <<: [ *id002, *id001 ]";
        System.out.println(input);
        Yaml yaml = new Yaml();
        try {
            List<Data> list = (List<Data>) yaml.load(input);

            // First object: Data ( 11, "id123" )
            assertEquals(11, list.get(0).getAge());
            assertEquals("id123", list.get(0).getId());

            // Second object: Data ( 37, null )
            assertEquals(37, list.get(1).getAge());
            assertEquals(null, list.get(1).getId());

            // Third object: Data ( 37, "id123" )
            assertEquals(37, list.get(2).getAge());
            assertEquals("id123", list.get(2).getId());
            fail("issue 100");
        } catch (Exception e) {
            // TODO issue 100
            System.out.println(e.getMessage());
        }
    }
}
