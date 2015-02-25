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
package org.yaml.snakeyaml.issues.issue100;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class MergeJavaBeanTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testNoMerge() {
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

    public void testMergeWithTags() {
        String input = Util.getLocalResource("issues/issue100-1.yaml");
        // System.out.println(input);
        Yaml yaml = new Yaml();

        List<?> list = (List<?>) yaml.load(input);
        // First object: Data ( 11, "id123" )
        assertEquals(list.get(0).getClass(), Data.class);
        assertEquals(11, ((Data) list.get(0)).getAge());
        assertEquals("id123", ((Data) list.get(0)).getId());

        // Second object: Data ( 13, "id456" )
        assertEquals(list.get(1).getClass(), Data.class);
        assertEquals(13, ((Data) list.get(1)).getAge());
        assertEquals("id456", ((Data) list.get(1)).getId());

        // Third object: Data ( 11, "id789" )
        assertEquals(list.get(2).getClass(), Data.class);
        assertEquals(11, ((Data) list.get(2)).getAge());
        assertEquals("id789", ((Data) list.get(2)).getId());

        // 4th object: DataMore ( 30, "id123" )
        assertEquals(list.get(3).getClass(), DataMore.class);
        assertEquals(30, ((DataMore) list.get(3)).getAge());
        assertEquals("id123", ((DataMore) list.get(3)).getId());
        assertTrue(((DataMore) list.get(3)).isComplete());

        // 5th object: Map ( age: 100 )
        assertTrue(list.get(4) instanceof Map);
        assertEquals(1, ((Map<?, ?>) list.get(4)).size());
        assertTrue(((Map<?, ?>) list.get(4)).containsKey("age"));
        assertEquals(100, ((Map<?, ?>) list.get(4)).get("age"));

        // 6th object: DataMore ( 100, "id789" )
        assertEquals(list.get(5).getClass(), DataMore.class);
        assertEquals(100, ((DataMore) list.get(5)).getAge());
        assertEquals("id789", ((DataMore) list.get(5)).getId());
        assertFalse(((DataMore) list.get(5)).isComplete());
        // All instances except the last Map must be different Data instances
        Set<Data> dataSet = new HashSet<Data>();
        for (Object data : list) {
            if (data instanceof Data) {
                dataSet.add((Data) data);
            }
        }
        assertEquals("Must be all but one Data instances.", list.size() - 1, dataSet.size());
    }

    /**
     * Since to explicit tag is provided JavaBean properties are used to create
     * a map
     */
    @SuppressWarnings("unchecked")
    public void testMergeBeanToMap() {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- << : *id001";
        // System.out.println(input);
        Yaml yaml = new Yaml();
        List<Object> list = (List<Object>) yaml.load(input);
        // First object: Data ( 11, "id123" )
        Data first = (Data) list.get(0);
        assertEquals(11, first.getAge());
        assertEquals("id123", first.getId());
        // Second object is map
        Map<?, ?> second = (Map<?, ?>) list.get(1);
        assertEquals(11, second.get("age"));
        assertEquals("id123", second.get("id"));
    }

    @SuppressWarnings("unchecked")
    public void testMergeAndDeviate() {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- <<: *id001\n  id: id456";
        // System.out.println(input);
        Yaml yaml = new Yaml();
        List<Object> list = (List<Object>) yaml.load(input);
        // First object: Data ( 11, "id123" )
        Data first = (Data) list.get(0);
        assertEquals(11, first.getAge());
        assertEquals("id123", first.getId());
        // Second object is map with a diffrent id
        Map<?, ?> second = (Map<?, ?>) list.get(1);
        assertEquals(11, second.get("age"));
        assertEquals("id456", second.get("id"));
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
    public void testMergeAndDeviateOverride() {
        String input = "- &id001 !!org.yaml.snakeyaml.issues.issue100.Data {age: 11, id: id123}\n- &id002 !!org.yaml.snakeyaml.issues.issue100.Data {age: 37}\n- <<: [ *id002, *id001 ]";
        // System.out.println(input);
        Yaml yaml = new Yaml();
        List<Data> list = (List<Data>) yaml.load(input);

        // First object: Data ( 11, "id123" )
        assertEquals(11, list.get(0).getAge());
        assertEquals("id123", list.get(0).getId());

        // Second object: Data ( 37, null )
        assertEquals(37, list.get(1).getAge());
        assertEquals(null, list.get(1).getId());

        // Third object: map
        Map<?, ?> third = (Map<?, ?>) list.get(2);
        assertEquals(37, third.get("age"));
        assertEquals("id123", third.get("id"));
    }

    /**
     * When the merged JavaBean is itself a JavaBean property then explicit tag
     * is not required
     */
    public void testMergeBeanProperty() {
        String input = Util.getLocalResource("issues/issue100-3.yaml");
        // System.out.println(input);
        Yaml yaml = new Yaml(new Constructor(DataBean.class));
        DataBean bean = (DataBean) yaml.load(input);
        assertEquals("id001", bean.getId());
        assertEquals("id002", bean.getData().getId());
        assertEquals(17, bean.getData().getAge());
        assertEquals(17, bean.getMore().getAge());
        assertEquals("more003", bean.getMore().getId());
        assertTrue(bean.getMore().isComplete());
    }

    /**
     * Merge map to JavaBean
     */
    @SuppressWarnings("unchecked")
    public void testMergeMapToJavaBean() {
        String input = "- &id001 { age: 11, id: id123 }\n- !!org.yaml.snakeyaml.issues.issue100.Data\n  <<: *id001\n  id: id456";
        // System.out.println(input);
        Yaml yaml = new Yaml(new Constructor());
        List<Object> objects = (List<Object>) yaml.load(input);
        assertEquals(2, objects.size());
        // Check first type
        Object first = objects.get(0);
        Map<Object, Object> firstMap = (Map<Object, Object>) first;
        // Check first contents
        assertEquals(11, firstMap.get("age"));
        assertEquals("id123", firstMap.get("id"));
        // Check second contents
        Data secondData = (Data) objects.get(1);
        assertEquals(11, secondData.getAge());
        assertEquals("id456", secondData.getId());
    }
}
