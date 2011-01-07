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
    public void testMergeToIdenticalInstances() throws IOException {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- << : *id001";
        System.out.println(input);
        Yaml yaml = new Yaml();
        try {
            List<Data> list = (List<Data>) yaml.load(input);
            for (Data data : list) {
                // System.out.println(data);
                assertEquals("id123", data.getId());
                assertEquals(11, data.getAge());
            }
        } catch (Exception e) {
            // TODO issue 100
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
        } catch (Exception e) {
            // TODO issue 100
        }
    }
}
